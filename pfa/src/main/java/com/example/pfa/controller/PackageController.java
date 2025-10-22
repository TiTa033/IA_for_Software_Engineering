package com.example.pfa.controller;

import com.example.pfa.entities.Capacity;
import com.example.pfa.entities.Packaging;
import com.example.pfa.services.GwpPredictionService;
import com.example.pfa.services.IPackageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/pfaController")
@CrossOrigin(origins = "http://localhost:4200")

public class PackageController {
    private final IPackageService packageService;
    private final GwpPredictionService gwp;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Packaging> addPackage(
            @RequestPart("packaging") String packagingJson,
            @RequestPart("image") MultipartFile imageFile) {

        try {
            // Debug logs
            System.out.println("Received JSON: " + packagingJson);
            System.out.println("Received file: " + imageFile.getOriginalFilename());

            // Parse JSON string into Packaging object
            ObjectMapper objectMapper = new ObjectMapper();
            Packaging packaging = objectMapper.readValue(packagingJson, Packaging.class);

            System.out.println("Parsed Packaging Object: " + packaging);

            // Save Packaging with image
            Packaging savedPackaging = packageService.addPackage(packaging, imageFile);
            return new ResponseEntity<>(savedPackaging, HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping(value = "/update-package/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Packaging> updatePackage(
            @PathVariable Long id,
            @RequestPart("packaging") String packagingJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            Packaging updatedPackage = packageService.updatePackage(id, packagingJson, imageFile);
            return new ResponseEntity<>(updatedPackage, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getAll")
    public List<Packaging> getAllPackagings(){

        return packageService.getAllPackages();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePackage(@PathVariable Long id){
        packageService.deletePackage(id);
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // Create path to the image file
            Path imagePath = Paths.get("uploads/" + imageName).toAbsolutePath();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/retrieve/{id}")
    public Optional<Packaging> retrieve(@PathVariable Long id){
        return packageService.getPackagingById(id);
    }

    @PostMapping("/ai")
    public ResponseEntity<Double> predictGwp(@RequestBody String productDetailsJson) {
        try {
            // Ensure the JSON string is properly escaped if needed
            productDetailsJson = productDetailsJson.replace("'", "\"");

            // Call the service to get the GWP prediction using the raw JSON string
            double prediction = gwp.predictGwp(productDetailsJson);

            // Return the prediction as a response
            return ResponseEntity.ok(prediction);
        } catch (Exception e) {
            // Handle errors gracefully
            return ResponseEntity.status(500).body(null);
        }
    }


}
