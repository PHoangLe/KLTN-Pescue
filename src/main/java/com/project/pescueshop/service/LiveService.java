package com.project.pescueshop.service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.project.pescueshop.model.dto.CreateLiveSessionRequest;
import com.project.pescueshop.model.entity.Merchant;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.model.entity.live.RecordingData;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.repository.dao.LiveSessionDAO;
import com.project.pescueshop.util.constant.EnumLiveStatus;
import com.project.pescueshop.util.constant.EnumResponseCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;

@Service
@Slf4j
@RequiredArgsConstructor
public class LiveService {

    public static final String MODERATOR_TOKEN_NAME = "ovCallModeratorToken";
    public static final String PARTICIPANT_TOKEN_NAME = "ovCallParticipantToken";
    public Map<String, RecordingData> moderatorsCookieMap = new HashMap<String, RecordingData>();
    public Map<String, List<String>> participantsCookieMap = new HashMap<String, List<String>>();

    @Value("${OPENVIDU_URL}")
    public String OPENVIDU_URL;

    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;
    private String edition;
    private final LiveSessionDAO liveSessionDAO;

    private final FileUploadService fileUploadService;
    private final LiveItemService liveItemService;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    public String getBasicAuth() {
        String stringToEncode = "OPENVIDUAPP:" + OPENVIDU_SECRET;
        String encodedString = Base64.getEncoder().encodeToString(stringToEncode.getBytes());
        return "Basic " + new String(encodedString);
    }

    public boolean isPRO() {
        return this.edition.toUpperCase().equals("PRO");
    }

    public boolean isCE() {
        return this.edition.toUpperCase().equals("CE");
    }

    public long getDateFromCookie(String recordingToken) {
        try {
            if (!recordingToken.isEmpty()) {
                MultiValueMap<String, String> cookieTokenParams = UriComponentsBuilder.fromUriString(recordingToken)
                        .build()
                        .getQueryParams();
                String date = cookieTokenParams.get("createdAt").get(0);
                return Long.parseLong(date);
            } else {
                return System.currentTimeMillis();
            }
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }

    public String getSessionIdFromCookie(String cookie) {
        try {

            if (!cookie.isEmpty()) {
                MultiValueMap<String, String> cookieTokenParams = UriComponentsBuilder.fromUriString(cookie)
                        .build().getQueryParams();
                return cookieTokenParams.get("sessionId").get(0);
            }

        } catch (Exception error) {
            log.error(error.getMessage());
        }
        return "";

    }

    public String getSessionIdFromRecordingId(String recordingId) {
        return recordingId.split("~")[0];
    }

    public boolean isModeratorSessionValid(String sessionId, String token) {
        try {

            if(token.isEmpty()) return false;
            if(!this.moderatorsCookieMap.containsKey(sessionId)) return false;

            MultiValueMap<String, String> storedTokenParams = UriComponentsBuilder
                    .fromUriString(this.moderatorsCookieMap.get(sessionId).getToken()).build().getQueryParams();

            MultiValueMap<String, String> cookieTokenParams = UriComponentsBuilder
                    .fromUriString(token).build().getQueryParams();

            String cookieSessionId = cookieTokenParams.get("sessionId").get(0);
            String cookieToken = cookieTokenParams.get(MODERATOR_TOKEN_NAME).get(0);
            String cookieDate = cookieTokenParams.get("createdAt").get(0);

            String storedToken = storedTokenParams.get(MODERATOR_TOKEN_NAME).get(0);
            String storedDate = storedTokenParams.get("createdAt").get(0);

            return sessionId.equals(cookieSessionId) && cookieToken.equals(storedToken)
                    && cookieDate.equals(storedDate);

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isParticipantSessionValid(String sessionId, String cookie) {

        try {
            if (!this.participantsCookieMap.containsKey(sessionId))	return false;
            if(cookie.isEmpty()) return false;


            MultiValueMap<String, String> cookieTokenParams = UriComponentsBuilder
                    .fromUriString(cookie).build().getQueryParams();

            List<String> storedTokens = this.participantsCookieMap.get(sessionId);

            String cookieSessionId = cookieTokenParams.get("sessionId").get(0);
            String cookieToken = cookieTokenParams.get(PARTICIPANT_TOKEN_NAME).get(0);
            String cookieDate = cookieTokenParams.get("createdAt").get(0);

            for (String token : storedTokens) {
                MultiValueMap<String, String> storedTokenParams = UriComponentsBuilder
                        .fromUriString(token).build().getQueryParams();

                String storedToken = storedTokenParams.get(PARTICIPANT_TOKEN_NAME).get(0);
                String storedDate = storedTokenParams.get("createdAt").get(0);

                if (sessionId.equals(cookieSessionId) && cookieToken.equals(storedToken) && cookieDate.equals(storedDate)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public Session createSession(CreateLiveSessionRequest request, User user) throws OpenViduJavaClientException {
        String sessionKey = UUID.randomUUID().toString();

        String thumbnailURL = fileUploadService.uploadFile(request.getThumbnail(), "live-thumbnail/", sessionKey);

        Session session = createSession(sessionKey);
        LiveSession liveSession = LiveSession.builder()
                .sessionKey(sessionKey)
                .userId(user.getUserId())
                .title(request.getLiveSessionTitle())
                .startTime(new Date())
                .thumbnail(thumbnailURL)
                .status(EnumLiveStatus.ACTIVE.getValue())
                .build();

        liveSessionDAO.saveAndFlushLiveSession(liveSession);

        CompletableFuture.runAsync(() -> {
            try {
                liveItemService.addLiveItem(liveSession, request.getLiveItemList());
            } catch (FriendlyException e) {
                log.error("Error creating connection: " + e.getMessage());
            }
        });
        return session;
    }

    private Session createSession(String sessionKey)
        throws OpenViduJavaClientException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("sessionKey", sessionKey);
            SessionProperties properties = SessionProperties.fromJson(params).build();
            Session session = openvidu.createSession(properties);
            session.fetch();
            return session;
        } catch (OpenViduHttpException e) {
            log.error("Error creating session: " + e.getMessage());
        }
        return null;
    }
    
    public Connection createConnection(String sessionKey, User user, OpenViduRole role) throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionKey);
        String nickname = user != null ? user.getUserFirstName() + " " + user.getUserLastName() : getRandomNickname();
        
        return createConnection(session, nickname, role);
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
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();

        try {
            Connection connection = session.createConnection(properties);

            MultiValueMap<String, String> tokenParams = UriComponentsBuilder
                    .fromUriString(connection.getToken()).build().getQueryParams();

            if (tokenParams.containsKey("edition")) {
                this.edition = tokenParams.get("edition").get(0);
            } else {
                this.edition = "ce";
            }

            return connection;
        } catch (OpenViduHttpException e) {
            log.error("Error creating connection: " + e.getMessage());
            throw e;
        }
    }

    public void endLiveSession(String sessionKey) throws FriendlyException, OpenViduJavaClientException, OpenViduHttpException {
        User user = AuthenticationService.getCurrentLoggedInUser();

        LiveSession liveSession = liveSessionDAO.findBySessionKey(sessionKey);

        if (liveSession == null) {
            throw new FriendlyException(EnumResponseCode.LIVE_SESSION_NOT_FOUND);
        }

        if (!AuthenticationService.isCurrentAdmin() && !liveSession.getUserId().equals(user.getUserId())) {
            throw new FriendlyException(EnumResponseCode.UNAUTHORIZED);
        }
        try {
            Session session = openvidu.getActiveSession(sessionKey);
            session.close();
        } catch (OpenViduHttpException | OpenViduJavaClientException e) {
            log.error("Error ending session: " + e.getMessage());
            throw e;
        }

        liveSession.setStatus(EnumLiveStatus.DONE.getValue());
        liveSession.setEndTime(new Date());

        liveSessionDAO.saveAndFlushLiveSession(liveSession);
    }
}

