package com.project.pescueshop.service.live;

import java.util.*;

import com.project.pescueshop.model.dto.CreateLiveSessionRequest;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.FileUploadService;
import com.project.pescueshop.service.MerchantService;
import com.project.pescueshop.util.constant.EnumLiveStatus;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiveService {

    @Value("${OPENVIDU_URL}")
    public String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;
    private final MerchantService merchantService;
    private final LiveSessionService liveSessionService;
    private final LiveCartService liveCartService;
    private final FileUploadService fileUploadService;
    private final LiveItemService liveItemService;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    public LiveSession createSession(CreateLiveSessionRequest request, MultipartFile thumbnail, User user) throws FriendlyException {
        String sessionKey = UUID.randomUUID().toString();
        String thumbnailURL = thumbnail != null ? fileUploadService.uploadFile(thumbnail, "live/thumbnail/", sessionKey) : "";

        Merchant merchant = merchantService.getMerchantByUserId(user.getUserId());

        try {
            Session session = createSession(sessionKey);
            assert session != null;

            LiveSession liveSession = LiveSession.builder()
                    .sessionKey(session.getSessionId())
                    .userId(user.getUserId())
                    .merchantId(merchant.getMerchantId())
                    .title(request.getLiveSessionTitle())
                    .startTime(new Date())
                    .thumbnail(thumbnailURL)
                    .status(EnumLiveStatus.ACTIVE.getValue())
                    .build();

            liveSessionService.saveAndFlushLiveSession(liveSession);

            liveItemService.addLiveItemAsync(liveSession, request.getLiveItemList());
            return liveSession;
        }
        catch (OpenViduJavaClientException e) {
            log.error("Error creating session: " + e.getMessage());
            throw new FriendlyException(EnumResponseCode.SYSTEM_ERROR);
        }
    }

    private Session createSession(String sessionKey)
        throws OpenViduJavaClientException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("customSessionId", sessionKey);

            SessionProperties properties = SessionProperties.fromJson(params).build();

            Session session = openvidu.createSession(properties);
            session.fetch();
            return session;
        } catch (OpenViduHttpException e) {
            log.error("Error creating session: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error creating session: " + e.getMessage());
        }

        return null;
    }
    
    public Connection createConnection(String sessionId, User user, String role) throws OpenViduJavaClientException, OpenViduHttpException, FriendlyException {
        LiveSession liveSession = liveSessionService.findBySessionId(sessionId);

        if (liveSession == null) {
            throw new FriendlyException(EnumResponseCode.LIVE_SESSION_NOT_FOUND);
        }

        OpenViduRole openViduRole = switch (role) {
            case "MODERATOR" -> OpenViduRole.MODERATOR;
            case "SUBSCRIBER" -> OpenViduRole.SUBSCRIBER;
            default -> OpenViduRole.PUBLISHER;
        };

        Session session = openvidu.getActiveSession(liveSession.getSessionKey());

        if (session == null) {
            throw new FriendlyException(EnumResponseCode.LIVE_SESSION_NOT_FOUND);
        }

        String nickname = user != null ? user.getUserFirstName() + " " + user.getUserLastName() : getRandomNickname();
        String userId = user != null ? user.getUserId() : null;

        liveCartService.createCartForNewUserAsync(userId, sessionId);
        
        return createConnection(session, nickname, openViduRole);
    }

    private String getRandomNickname() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000, 900000);
        return "Guest" + randomNumber;
    }

    private Connection createConnection(Session session, String nickname, OpenViduRole role)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> connectionData = new HashMap<>();

        connectionData.put("openviduCustomConnectionId", nickname);
        params.put("role", role.name());
        params.put("data", connectionData.toString());

        ConnectionProperties properties = new ConnectionProperties.Builder().build();

        try {
            return session.createConnection(properties);
        } catch (OpenViduHttpException e) {
            log.error("Error creating connection: " + e.getMessage());
            throw e;
        }
    }

    public void endLiveSession(String sessionId) throws FriendlyException, OpenViduJavaClientException, OpenViduHttpException {
        User user = AuthenticationService.getCurrentLoggedInUser();

        LiveSession liveSession = liveSessionService.findBySessionId(sessionId);

        if (liveSession == null) {
            throw new FriendlyException(EnumResponseCode.LIVE_SESSION_NOT_FOUND);
        }

        if (!AuthenticationService.isCurrentAdmin() && !liveSession.getUserId().equals(user.getUserId())) {
            throw new FriendlyException(EnumResponseCode.UNAUTHORIZED);
        }
        try {
            Session session = openvidu.getActiveSession(liveSession.getSessionKey());
            session.close();
        } catch (OpenViduHttpException | OpenViduJavaClientException e) {
            log.error("Error ending session: {}", e.getMessage());
        }

        liveSession.setStatus(EnumLiveStatus.DONE.getValue());
        liveSession.setEndTime(new Date());

        liveSessionService.saveAndFlushLiveSession(liveSession);
    }

    public void processWebHooks(Map<String, Object> bodyMap) {
        String event = bodyMap.get("event").toString();
        switch (event) {
            case "sessionDestroyed" -> processLiveDestroyed(bodyMap);
            case "sessionCreated" -> processLiveCreated(bodyMap);
        }
    }

    private void processLiveCreated(Map<String, Object> bodyMap) {
        int timestamp = Integer.parseInt(bodyMap.get("timestamp").toString());
        String sessionKey = bodyMap.get("sessionId").toString();

        log.info("openvidu webhook: processLiveCreated: sessionKey {} timestamp {}", sessionKey, timestamp);
    }

    private void processLiveDestroyed(Map<String, Object> bodyMap) {
        int timestamp = Integer.parseInt(bodyMap.get("timestamp").toString());
        String sessionKey = bodyMap.get("sessionId").toString();
        String reason = bodyMap.get("reason").toString();

        LiveSession liveSession = liveSessionService.findBySessionKey(sessionKey);
        if (liveSession == null){
            log.error("openvidu webhook: processLiveDestroyed: liveSession NotFound sessionKey: {}", sessionKey);
            return;
        }
        log.info("openvidu webhook: processLiveDestroyed: sessionKey {} timestamp {} reason {}", sessionKey, timestamp, reason);

        liveSession.setStatus(EnumLiveStatus.DONE.getValue());
        liveSession.setEndTime(new Date(timestamp));

        liveSessionService.saveAndFlushLiveSession(liveSession);
    }
}

