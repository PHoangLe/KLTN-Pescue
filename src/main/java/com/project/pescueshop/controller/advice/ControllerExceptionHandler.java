package com.project.pescueshop.controller.advice;

import com.project.pescueshop.model.dto.ErrorLogDTO;
import com.project.pescueshop.model.dto.general.ResponseDTO;
import com.project.pescueshop.model.exception.FriendlyException;
import com.project.pescueshop.model.exception.UnauthenticatedException;
import com.project.pescueshop.model.entity.ErrorLog;
import com.project.pescueshop.repository.ErrorLogRepository;
import com.project.pescueshop.service.AuthenticationService;
import com.project.pescueshop.util.constant.EnumResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {
    private final AuthenticationService authenticationService;
    private final ErrorLogRepository errorLogRepository;

    @ExceptionHandler(FriendlyException.class)
    public ResponseEntity<ResponseDTO<ErrorLogDTO>> friendlyExceptionHandler(FriendlyException ex, WebRequest request) {
        log.trace(request.getDescription(false));
        log.trace(ex.getStatusCode().getStatusCode());
        return ResponseEntity.ok(new ResponseDTO<>(ex.getStatusCode()));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ResponseDTO<ErrorLogDTO>> unauthenticatedExceptionHandler(UnauthenticatedException ex, WebRequest request) {

        return ResponseEntity.ok(new ResponseDTO<>(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorLogDTO>> globalExceptionHandler(Exception ex, WebRequest request) {

        ErrorLog errorLog = errorLogRepository.save(ErrorLog.builder()
                .url(request.getDescription(false))
                .locale(request.getLocale().toString())
                .stackTrace(ExceptionUtils.getStackTrace(ex))
                .message(ex.getMessage())
                .date(new Date())
                .client(request.getRemoteUser())
                .build());

        return ResponseEntity.ok(new ResponseDTO<>(EnumResponseCode.SYSTEM_ERROR, new ErrorLogDTO(errorLog)));
    }
}
