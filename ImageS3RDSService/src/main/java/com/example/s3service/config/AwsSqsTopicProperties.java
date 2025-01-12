package com.example.s3service.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.aws.sqs")
@Component
public class AwsSqsTopicProperties {

    @NotBlank(message = "SQS queue URL must not be blank")
    private String queueUrl;

}