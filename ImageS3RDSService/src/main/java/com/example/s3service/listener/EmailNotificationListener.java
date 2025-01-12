package com.example.s3service.listener;

import com.example.s3service.service.NotificationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationListener {

    private final NotificationService notificationService;

    @SqsListener("${io.reflectoring.aws.sqs.queue-url}")
    public void listen(List<String> messages) {
        log.info("Received batch sqs message with size: {}", messages.size());
        messages.forEach(notificationService::sendSnsMessage);
    }

}