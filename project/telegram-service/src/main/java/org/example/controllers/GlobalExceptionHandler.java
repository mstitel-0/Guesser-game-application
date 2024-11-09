package org.example.controllers;

import org.example.exceptions.UnexpectedCommandException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnexpectedCommandException.class)
    public ResponseEntity<String> handleUnexpectedCommandException(UnexpectedCommandException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
    }
}
