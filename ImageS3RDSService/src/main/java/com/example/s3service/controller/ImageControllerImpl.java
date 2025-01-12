package com.example.s3service.controller;

import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import com.example.s3service.service.ImageMetadataService;
import com.example.s3service.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@CrossOrigin
@RequestMapping("v1/images")
@RequiredArgsConstructor
public class ImageControllerImpl {

    private final ImageService imageService;

    private final ImageMetadataService imageMetadataService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageMetadataResponse> getImageMetadata(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "isRandom", required = false, defaultValue = "false") Boolean isRandom) {
        if (TRUE.equals(isRandom)) {
            return new ResponseEntity<>(imageMetadataService.getRandonImageMetadata(), HttpStatus.OK);
        }

        return new ResponseEntity<>(imageMetadataService.getImageMetadata(name), HttpStatus.OK) ;
    }

    @GetMapping(value = "/{name}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> getImage(@PathVariable String name) {
        return new ResponseEntity<>(imageService.downloadImage(name), HttpStatus.OK);
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> save(@ModelAttribute ImageUploadRequest imageUploadRequest) throws IOException {
        return new ResponseEntity<>(imageService.uploadImage(imageUploadRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        imageService.deleteImage(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
