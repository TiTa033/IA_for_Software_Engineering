package com.example.pfa.services;

import com.example.pfa.entities.Packaging;
import com.example.pfa.repositories.PackageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PackageServiceImpl implements IPackageService {

    @Autowired
    private PackageRepository packageRepository;

    public Packaging addPackage(Packaging packaging, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());

                packaging.setImageUrl(fileName);
            }
            return packageRepository.save(packaging);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public List<Packaging> getAllPackages() {
        return packageRepository.findAll();
    }

    public void deletePackage(Long packageId) {
        packageRepository.deleteById(packageId);
    }

    public Packaging updatePackage(Long id, String packagingJson, MultipartFile imageFile) {
        try {
            Packaging existingPackage = packageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Packaging not found with id: " + id));

            // Parse the JSON into a Packaging object
            ObjectMapper objectMapper = new ObjectMapper();
            Packaging updatedData = objectMapper.readValue(packagingJson, Packaging.class);

            // Update fields
            existingPackage.setName(updatedData.getName());
            existingPackage.setColor(updatedData.getColor());
            existingPackage.setPrice(updatedData.getPrice());
            existingPackage.setCapacity(updatedData.getCapacity());

            // If there's a new image, upload and replace the old one
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "uploads/";
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, imageFile.getBytes());

                existingPackage.setImageUrl(fileName);
            }

            return packageRepository.save(existingPackage);

        } catch (IOException e) {
            throw new RuntimeException("Failed to update packaging", e);
        }
    }

    public Optional<Packaging> getPackagingById(Long id) {
        Optional<Packaging> p = packageRepository.findById(id);
        return p;
    }


}
