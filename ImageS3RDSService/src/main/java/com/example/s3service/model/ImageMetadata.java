package com.example.s3service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="image_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetadata {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "image_name", length = 20, unique = true)
    private String imageName;

    @Column(name = "size_in_bytes")
    private Integer sizeInBytes;

    @Column(name = "file_extension", length = 40)
    private String fileExtension;

    @Column(name = "last_update",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            insertable = false,
            updatable = false)
    private LocalDateTime lastUpdate;

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", sizeInBytes=" + sizeInBytes +
                ", fileExtention='" + fileExtension + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
