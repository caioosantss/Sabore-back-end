package com.projeto_final.receitas.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(resourceNotFoundException.class)
    public ResponseEntity<standardError> resourceNotFound (resourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        standardError err = new standardError(
                Instant.now(),
                status.value(),
                "Recurso não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(businessException.class)
    public ResponseEntity<standardError> businessError(businessException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        standardError err = new standardError(
                Instant.now(),
                status.value(),
                "Regra de negócio violada",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
}
