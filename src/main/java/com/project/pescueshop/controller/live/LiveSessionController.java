package com.project.pescueshop.controller.live;

import com.project.pescueshop.model.dto.CreateLiveSessionRequest;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.User;
import com.project.pescueshop.model.entity.live.LiveSession;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.service.live.LiveService;
import com.project.pescueshop.service.live.LiveSessionService;
import com.project.pescueshop.util.constant.EnumResponseCode;

import io.openvidu.java.client.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/live/sessions")
@Slf4j
public class LiveSessionController {
    private final LiveService liveService;
    private final LiveSessionService liveSessionService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> initializeSession(@RequestPart CreateLiveSessionRequest request,@RequestPart(required = false) MultipartFile thumbnail)
            throws FriendlyException, OpenViduJavaClientException {
        User user = AuthenticationService.getCurrentLoggedInUser();
        LiveSession session = liveService.createSession(request, thumbnail, user);

        ResponseDTO<String> resp = new ResponseDTO<>(EnumResponseCode.SUCCESS, session.getSessionId(), "sessionId");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{sessionKey}/connections")
    public ResponseEntity<ResponseDTO<String>> createConnection(@PathVariable String sessionKey, @RequestBody String role)
            throws OpenViduJavaClientException, OpenViduHttpException, FriendlyException {
        User user = AuthenticationService.getCurrentLoggedInUserIfExist();
        Connection connection = liveService.createConnection(sessionKey, user, role);

        ResponseDTO<String> resp = new ResponseDTO<>(EnumResponseCode.SUCCESS, connection.getToken());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("")
    public ResponseEntity<ResponseDTO<Page<LiveSession>>> getActiveLiveSession(
            @RequestParam(defaultValue = "1") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "true") boolean isOnlyActive) {
        isOnlyActive = !AuthenticationService.isCurrentAdmin() || isOnlyActive;

        Pageable pageable = PageRequest.of(offset - 1, limit);
        Page<LiveSession> liveSession = liveSessionService.getAllActiveLiveSession(isOnlyActive, pageable);

        ResponseDTO<Page<LiveSession>> resp = new ResponseDTO<>(EnumResponseCode.SUCCESS, liveSession);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{sessionId}/end")
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ResponseDTO<String>> endLiveSession(@PathVariable String sessionId) throws FriendlyException, OpenViduJavaClientException, OpenViduHttpException {
        liveService.endLiveSession(sessionId);

        ResponseDTO<String> resp = new ResponseDTO<>(EnumResponseCode.SUCCESS, "End live successfully");
        return ResponseEntity.ok(resp);
    }
}
