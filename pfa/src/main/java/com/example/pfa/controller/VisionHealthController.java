package com.example.pfa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vision")
public class VisionHealthController {

    @GetMapping("/status")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"spring-ok\"}");
    }
}
