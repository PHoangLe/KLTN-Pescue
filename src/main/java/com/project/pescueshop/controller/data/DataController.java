package com.project.pescueshop.controller.data;

import com.cloudinary.utils.StringUtils;
import com.project.pescueshop.model.dto.InvoiceDataDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.elastic.document.InvoiceData;
import com.project.pescueshop.model.elastic.document.RatingData;
import com.project.pescueshop.model.entity.ViewAuditLog;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.service.data.DataService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Api
public class DataController {
    private final DataService dataService;

    @GetMapping("/views-audit-log/{objectId}")
    public ResponseEntity<ResponseDTO<List<ViewAuditLog>>> getViewsAudiLogData(@PathVariable String objectId,
                                                                               @RequestHeader("client-id") String clientId, @RequestHeader("client-key") String clientKey) throws FriendlyException {
        if (!validatingClient(clientId, clientKey)){
            log.info("method: GET path: /api/v1/data/views-audit-logs/{} client id or client key invalid", objectId);
            throw new FriendlyException(EnumResponseCode.CLIENT_ID_OR_CLIENT_KEY_INVALID);
        }

        List<ViewAuditLog> resp = dataService.getViewsAudiLogData(objectId);
        return ResponseEntity.ok(new ResponseDTO<>(EnumResponseCode.SUCCESS, resp, "views"));
    }

    @GetMapping("/invoice")
    public ResponseEntity<ResponseDTO<List<InvoiceData>>> getInvoiceData(@RequestHeader("client-id") String clientId, @RequestHeader("client-key") String clientKey) throws FriendlyException {
        if (!validatingClient(clientId, clientKey)){
            log.info("method: GET path: /api/v1/data/invoice client id or client key invalid");
            throw new FriendlyException(EnumResponseCode.CLIENT_ID_OR_CLIENT_KEY_INVALID);
        }

        List<InvoiceData> resp = dataService.getInvoiceData();
        return ResponseEntity.ok(new ResponseDTO<>(EnumResponseCode.SUCCESS, resp, "invoiceData"));
    }

    @GetMapping("/rating")
    public ResponseEntity<ResponseDTO<List<RatingData>>> getRatingData(@RequestHeader("client-id") String clientId, @RequestHeader("client-key") String clientKey) throws FriendlyException {
        if (!validatingClient(clientId, clientKey)){
            log.info("method: GET path: /api/v1/data/rating client id or client key invalid");
            throw new FriendlyException(EnumResponseCode.CLIENT_ID_OR_CLIENT_KEY_INVALID);
        }

        List<RatingData> resp = dataService.getRatingData();
        return ResponseEntity.ok(new ResponseDTO<>(EnumResponseCode.SUCCESS, resp, "ratingData"));
    }

    private Boolean validatingClient(String clientId, String clientKey) {
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientKey)){
            return false;
        }

        return clientKey.equals("opIGrWw2u0WBmZHVIyDRqM6t0P2NKE1c") && clientId.equals("PqescSU7WscLlNRvHK2Ew397vBa0b7dr");
    }
}
