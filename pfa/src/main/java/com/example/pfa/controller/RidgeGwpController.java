package com.example.pfa.controller;

import com.example.pfa.dto.RidgeGwpOptimizeResponse;
import com.example.pfa.dto.RidgeGwpRequest;
import com.example.pfa.dto.RidgeGwpResponse;
import com.example.pfa.services.RidgeGwpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ridge-gwp")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class RidgeGwpController {
    
    private final RidgeGwpService ridgeGwpService;
    
    @PostMapping("/predict")
    public ResponseEntity<RidgeGwpResponse> predictGwp(@RequestBody RidgeGwpRequest request) {
        try {
            log.info("Received GWP prediction request: {}", request);
            
            // Validate request
            if (request.getWeight() <= 0 || request.getLength() <= 0 || 
                request.getWidth() <= 0 || request.getHeight() <= 0 || 
                request.getDistanceKm() < 0 || request.getMaterial() == null || 
                request.getMaterial().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            RidgeGwpResponse response = ridgeGwpService.predictGwp(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing GWP prediction request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/optimize")
    public ResponseEntity<RidgeGwpOptimizeResponse> optimizeGwp(@RequestBody RidgeGwpRequest request) {
        try {
            log.info("Received GWP optimization request: {}", request);
            
            // Validate request
            if (request.getWeight() <= 0 || request.getLength() <= 0 || 
                request.getWidth() <= 0 || request.getHeight() <= 0 || 
                request.getDistanceKm() < 0 || request.getMaterial() == null || 
                request.getMaterial().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            RidgeGwpOptimizeResponse response = ridgeGwpService.optimizeGwp(request);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing GWP optimization request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ridge-GWP service is running");
    }
}
