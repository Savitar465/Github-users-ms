package com.githubx.usersms.util.errorhandling;

import com.githubx.usersms.util.errorhandling.dto.ErrorResponse;
import com.githubx.usersms.util.errorhandling.exceptions.AuthTokenConvertException;
import com.githubx.usersms.util.errorhandling.exceptions.EntityConflictException;
import com.githubx.usersms.util.errorhandling.exceptions.EntityDeletedException;
import com.githubx.usersms.util.errorhandling.exceptions.EntityNotFoundException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MsUsersControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(Exception e) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerExceptions(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingBadRequestExceptions(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundExceptions(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleExceptions(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<ErrorResponse> handleEntityConflictExceptions(Exception e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthTokenConvertException.class)
    public ResponseEntity<ErrorResponse> handleAuthTokenConvertException(Exception e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }

    @ResponseStatus(HttpStatus.GONE)
    @ExceptionHandler(EntityDeletedException.class)
    public ResponseEntity<ErrorResponse> handleEntityDeletedException(Exception e) {
        HttpStatus status = HttpStatus.GONE;
        return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), StackTraceUtil.getStackTrace(e)), status);
    }
}
