package com.tusofia.codeinspection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorAttribute {
    private HttpStatus status;

    private String message;

    private List<String> errors;

    private LocalDateTime timestamp;
    public ErrorAttribute(final HttpStatus status, final String message, final String error, LocalDateTime timestamp) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
        this.timestamp = timestamp;
    }
}
