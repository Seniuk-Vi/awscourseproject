package com.awscourseproject.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqsToSnsLambda implements RequestHandler<SQSEvent, Void> {
    private static final Logger logger = LoggerFactory.getLogger(SqsToSnsLambda.class);

    private final SnsClient snsClient = SnsClient.builder()
            .region(Region.of(System.getenv(SdkSystemSetting.AWS_REGION.environmentVariable())))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    private final String snsTopicArn = System.getenv("SNS_TOPIC_ARN");


    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        context.getLogger().log("test log");
        context.getLogger().log("Received SQS event with " + event.getRecords().size() + " records");
        for (SQSEvent.SQSMessage sqsMessage : event.getRecords()) {
            String messageBody = sqsMessage.getBody();

            // Publish message to SNS
            PublishResponse publishResponse = publishMessage(messageBody);
            context.getLogger().log("Published message to SNS with ID: " + publishResponse.messageId());
        }
        return null;
    }

    private PublishResponse publishMessage(String message) {
        PublishRequest publishRequest = PublishRequest.builder()
                .message(message)
                .topicArn(snsTopicArn)
                .build();

        return snsClient.publish(publishRequest);
    }
}