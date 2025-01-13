package com.example.s3service.service.impl;

import com.example.s3service.config.AwsSnsTopicProperties;
import com.example.s3service.config.AwsSqsTopicProperties;
import com.example.s3service.service.NotificationService;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private static final String SNS_PROTOCOL = "email";

    private final SqsTemplate sqsTemplate;
    private final SnsClient snsClient;
    private final AwsSnsTopicProperties awsSnsTopicProperties;
    private final AwsSqsTopicProperties awsSqsTopicProperties;

    @Override
    public void subscribe(String email) {
        snsClient.subscribe(builder -> builder
                .protocol(SNS_PROTOCOL)
                .endpoint(email)
                .topicArn(awsSnsTopicProperties.getTopicArn()));
    }

    @Override
    public void unsubscribe(String email) {
        snsClient.unsubscribe(builder -> builder
                .subscriptionArn(email));
    }


    @Override
    public void sendSqsMessage(String message) {
        String queueUrl = awsSqsTopicProperties.getQueueUrl();
        log.info("Sending message to SQS: {}", message);
        sqsTemplate.send(queueUrl, message);
        log.info("Message sent to SQS: {}", message);
    }
}
