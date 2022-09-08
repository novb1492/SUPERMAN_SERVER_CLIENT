package com.kimcompany.jangbogbackendver2.Config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.awsAccessKey;

/**
 * sqs는 propertiesText.java보다 bean에 빨리 올라감
 * propertiesText.java 가 static이여서 빈순서 지정 어노테이션도 안먹힘
 * 방법은 propertiesText 에 Value어노테이션으로 해서 getter로 하는방법
 * 아니면 여기서 직접 주입하는 방법이 있는데 직접 주입 선택 
 */
@Configuration
public class SqsConfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secret;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Primary//빈 우선선택
    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret))).build();
    }
}
