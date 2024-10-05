package org.example.apigateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

   @GetMapping("/fallback")
   public ResponseEntity<String> fallback() {
       return new ResponseEntity<>("Authentication service is currently down", HttpStatus.SERVICE_UNAVAILABLE);
   }

}
