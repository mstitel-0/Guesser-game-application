package org.example.gameeservice.Controllers;

import org.example.gameeservice.Exceptions.GamesNotFoundException;
import org.example.gameeservice.Exceptions.InvalidTopicException;
import org.example.gameeservice.Exceptions.GameEndedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameEndedException.class)
    public ResponseEntity<String> gameEndedException(GameEndedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTopicException.class)
    public ResponseEntity<String> invalidTopicException(InvalidTopicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GamesNotFoundException.class)
    public ResponseEntity<String> gamesNotFoundException(GamesNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(GamesNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}
