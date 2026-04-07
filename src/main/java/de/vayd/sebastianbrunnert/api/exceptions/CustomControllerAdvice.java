package de.vayd.sebastianbrunnert.api.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * This class is a custom controller advice. It handles exceptions thrown by the controllers.
 */
@ControllerAdvice
public class CustomControllerAdvice {

    /**
     * When a ApiError is manully thrown, this method will handle it and return a ResponseEntity with the error message.
     * @param e The exception
     * @return ResponseEntity with the error message
     */
    @ExceptionHandler(ApiError.class)
    public ResponseEntity handleApiError(ApiError e) {
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    /**
     * When a exception that indicates a not found error is thrown, this method will handle it and return a ApiError with the message "NOT_FOUND".
     * @param e The exception
     * @return ResponseEntity with the error message
     */
    @ExceptionHandler({
            ClientAbortException.class,
            JsonProcessingException.class,
            JsonParseException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            NoHandlerFoundException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            EntityNotFoundException.class,
            JpaObjectRetrievalFailureException.class,
            NoResourceFoundException.class,
            NotFoundException.class
    })
    public ResponseEntity handleNotFound(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ApiError().setMessage("NOT_FOUND"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(new ApiError().setMessage("INTERNAL_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity handleAccessDenied(AccessDeniedException e) {
        return new ResponseEntity<>(new ApiError().setMessage("ACCESS_DENIED").setLevel(ApiError.Level.LOGOUT), HttpStatus.UNAUTHORIZED);
    }
}

