package com.example.s3service.api;

import com.example.s3service.payload.ImageMetadataResponse;
import com.example.s3service.payload.ImageResponse;
import com.example.s3service.payload.ImageUploadRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@CrossOrigin
@RequestMapping("v1/images")
public interface ImageController {

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageMetadataResponse> getImageMetadata(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "isRandom", required = false, defaultValue = "false") Boolean isRandom);

    @GetMapping(value = "/{name}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> getImage(@PathVariable String name);

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponse> save(@ModelAttribute ImageUploadRequest imageUploadRequest);

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name);
}
