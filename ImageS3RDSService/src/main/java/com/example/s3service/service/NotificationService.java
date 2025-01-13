package com.example.s3service.service;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

    void subscribe(String email);

    void unsubscribe(String email);

    void sendSqsMessage(String message);

}
