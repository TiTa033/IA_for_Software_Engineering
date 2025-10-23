package com.example.pfa.controller;

import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vision")
public class VisionController {

    private final WebClient visionClient;

    public VisionController(WebClient visionWebClient) {
        this.visionClient = visionWebClient; // injecté par WebClientConfig
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<String>> health() {
        return visionClient.get()
                .uri("/health")
                .retrieve()
                .toEntity(String.class);
    }

    @PostMapping(value = "/predict", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> predict(@RequestPart("file") MultipartFile file) {
        MultipartBodyBuilder mb = new MultipartBodyBuilder();
        mb.part("file", file.getResource())
                .filename(file.getOriginalFilename())
                .contentType(MediaType.parseMediaType(file.getContentType()));

        return visionClient.post()
                .uri("/predict")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(mb.build()))
                .retrieve()
                .toEntity(String.class);
    }

    @PostMapping("/reload")
    public Mono<ResponseEntity<String>> reload() {
        return visionClient.post()
                .uri("/reload")
                .retrieve()
                .toEntity(String.class);
    }
}
