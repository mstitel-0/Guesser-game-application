package org.example.apigateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

   @GetMapping("/fallback")
   public ResponseEntity<String> fallbackGet() {
       return new ResponseEntity<>("Authentication service is currently down (GET)", HttpStatus.SERVICE_UNAVAILABLE);
   }

    @PostMapping("/fallback")
    public ResponseEntity<String> fallbackPost() {
        return new ResponseEntity<>("Authentication service is currently down (POST)", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
