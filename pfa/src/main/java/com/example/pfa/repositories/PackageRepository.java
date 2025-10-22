package com.example.pfa.repositories;

import com.example.pfa.entities.Packaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Packaging,Long> {
}
