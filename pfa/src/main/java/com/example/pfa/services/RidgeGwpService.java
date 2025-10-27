package com.example.pfa.services;

import com.example.pfa.dto.RidgeGwpOptimizeResponse;
import com.example.pfa.dto.RidgeGwpRequest;
import com.example.pfa.dto.RidgeGwpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RidgeGwpService {
    
    @Value("${ridge-gwp.service.url:http://localhost:8000}")
    private String ridgeGwpServiceUrl;
    
    private final RestTemplate restTemplate;
    
    public RidgeGwpService() {
        this.restTemplate = new RestTemplate();
    }
    
    public RidgeGwpResponse predictGwp(RidgeGwpRequest request) {
        try {
            String url = ridgeGwpServiceUrl + "/predict";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<RidgeGwpRequest> entity = new HttpEntity<>(request, headers);
            
            log.info("Calling Ridge-GWP service at: {}", url);
            log.info("Request payload: {}", request);
            
            ResponseEntity<RidgeGwpResponse> response = restTemplate.postForEntity(
                url, entity, RidgeGwpResponse.class);
            
            log.info("Response received: {}", response.getBody());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error calling Ridge-GWP service", e);
            throw new RuntimeException("Failed to get GWP prediction: " + e.getMessage(), e);
        }
    }
    
    public RidgeGwpOptimizeResponse optimizeGwp(RidgeGwpRequest request) {
        try {
            String url = ridgeGwpServiceUrl + "/optimize";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<RidgeGwpRequest> entity = new HttpEntity<>(request, headers);
            
            log.info("Calling Ridge-GWP optimize service at: {}", url);
            log.info("Request payload: {}", request);
            
            ResponseEntity<RidgeGwpOptimizeResponse> response = restTemplate.postForEntity(
                url, entity, RidgeGwpOptimizeResponse.class);
            
            log.info("Optimize response received: {}", response.getBody());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error calling Ridge-GWP optimize service", e);
            throw new RuntimeException("Failed to optimize GWP: " + e.getMessage(), e);
        }
    }
}

