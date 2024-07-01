package com.project.pescueshop.controller.live;

import com.cloudinary.utils.StringUtils;
import com.project.pescueshop.service.live.LiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/openvidu/webhooks")
@Slf4j
public class OpenviduWebHookController {
    private final LiveService liveService;

    @PostMapping("")
    public ResponseEntity<Object> openviduWebhooks(
            @RequestBody Map<String, Object> bodyMap,
            @RequestHeader("client-id") String clientId,
            @RequestHeader("client-key") String clientKey){

        if (validatingClient(clientId, clientKey)){
            liveService.processWebHooks(bodyMap);
        } else{
            log.error("openvidu webhooks controller: invalid client id or client key");
        }

        return ResponseEntity.ok(null);
    }

    private Boolean validatingClient(String clientId, String clientKey) {
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientKey)){
            return false;
        }

        return clientKey.equals("opIGrWw2u0WBmZHVIyDRqM6t0P2NKE1c") && clientId.equals("PqescSU7WscLlNRvHK2Ew397vBa0b7dr");
    }
}
