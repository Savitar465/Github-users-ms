package com.inspire.msusuarios.util.errorhandling;

import com.inspire.mscommon.exceptionhandler.controller.GlobalControllerAdvice;
import com.inspire.mscommon.exceptionhandler.dto.ErrorResponse;
import com.inspire.mscommon.util.StackTraceUtil;
import com.inspire.msusuarios.util.errorhandling.exceptions.KeycloakException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MsUsuariosControllerAdvice extends GlobalControllerAdvice {

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }
}
