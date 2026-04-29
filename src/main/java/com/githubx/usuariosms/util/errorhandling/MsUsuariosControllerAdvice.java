package com.githubx.usuariosms.util.errorhandling;

import com.inspire.mscommon.exceptionhandler.dto.ErrorResponse;
import com.inspire.mscommon.util.StackTraceUtil;
import com.githubx.usuariosms.util.errorhandling.exceptions.KeycloakException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MsUsuariosControllerAdvice  {

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }
}
