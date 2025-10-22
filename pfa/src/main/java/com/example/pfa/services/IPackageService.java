package com.example.pfa.services;

import com.example.pfa.entities.Packaging;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IPackageService {

    public Packaging addPackage(Packaging packaging, MultipartFile imageFile) throws IOException;

    public List<Packaging> getAllPackages();
    public void deletePackage(Long packageId);
    public Packaging updatePackage(Long id, String packagingJson, MultipartFile imageFile);
    public Optional<Packaging> getPackagingById(Long id);
}
