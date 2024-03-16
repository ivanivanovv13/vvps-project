package com.tusofia.codeinspection.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ErrorAttribute errorAttribute = createErrorAttribute(ex, HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, errorAttribute, headers, errorAttribute.getStatus(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorAttribute> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorAttribute errorAttribute = new ErrorAttribute(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(),
                "Email or Password doesn't match!", LocalDateTime.now());
        return new ResponseEntity<>(errorAttribute, new HttpHeaders(), errorAttribute.getStatus());
    }

    @ExceptionHandler(TrainNotFoundException.class)
    public ResponseEntity<ErrorAttribute> handleTrainNotFoundException(TrainNotFoundException ex) {
        ErrorAttribute errorAttribute = new ErrorAttribute(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(),
                "Train not found by given id!", LocalDateTime.now());
        return new ResponseEntity<>(errorAttribute, new HttpHeaders(), errorAttribute.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorAttribute> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorAttribute errorAttribute = new ErrorAttribute(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(),
                "User not found by given id!", LocalDateTime.now());
        return new ResponseEntity<>(errorAttribute, new HttpHeaders(), errorAttribute.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorAttribute> handleUserAlreadyExist(UserAlreadyExistException ex) {
        ErrorAttribute errorAttribute = new ErrorAttribute(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),
                "User with given email already registered!", LocalDateTime.now());
        return new ResponseEntity<>(errorAttribute, new HttpHeaders(), errorAttribute.getStatus());
    }

    private ErrorAttribute createErrorAttribute(MethodArgumentNotValidException ex, HttpStatus status) {
        List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return new ErrorAttribute(status, ex.getLocalizedMessage(), errors, LocalDateTime.now());
    }
}
