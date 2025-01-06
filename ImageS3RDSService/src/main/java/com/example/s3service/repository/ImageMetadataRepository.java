package com.example.s3service.repository;

import com.example.s3service.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

    ImageMetadata findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image_metadata ORDER BY RAND() limit 1;")
    ImageMetadata findRandom();

    void deleteByName(String name);
}
