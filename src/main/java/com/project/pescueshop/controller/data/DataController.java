package com.project.pescueshop.controller.data;

import com.cloudinary.utils.StringUtils;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.data.DataService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
@CrossOrigin
@Api
public class DataController {
    private final DataService dataService;

    @GetMapping("/views-audit-log/{objectId}")
    public ResponseEntity<ResponseDTO<List<ViewAuditLog>>> getViewsAudiLogData(@PathVariable String objectId,
                                                                               @RequestHeader("client-id") String clientId, @RequestHeader("client-key") String clientKey) throws FriendlyException {
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientKey)){
            throw new FriendlyException(EnumResponseCode.CLIENT_ID_OR_CLIENT_KEY_INVALID);
        }

        if (!clientKey.equals("opIGrWw2u0WBmZHVIyDRqM6t0P2NKE1c") || !clientId.equals("PqescSU7WscLlNRvHK2Ew397vBa0b7dr")){
            throw new FriendlyException(EnumResponseCode.CLIENT_ID_OR_CLIENT_KEY_INVALID);
        }

        List<ViewAuditLog> resp = dataService.getViewsAudiLogData(objectId);
        return ResponseEntity.ok(new ResponseDTO<>(EnumResponseCode.SUCCESS, resp, "views"));
    }
}
