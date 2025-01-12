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
@ConfigurationProperties(prefix = "io.reflectoring.aws.sns")
@Component
public class AwsSnsTopicProperties {

    @NotBlank(message = "SNS topic ARN must be configured")
    private String topicArn;

}