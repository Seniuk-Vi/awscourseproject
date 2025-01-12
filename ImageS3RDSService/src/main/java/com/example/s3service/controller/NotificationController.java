package com.example.s3service.controller;

import com.example.s3service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribe(@RequestParam String email) {
        notificationService.subscribe(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam String email) {
        notificationService.unsubscribe(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
