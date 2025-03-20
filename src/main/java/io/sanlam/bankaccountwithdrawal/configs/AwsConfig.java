package io.sanlam.bankaccountwithdrawal.configs;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.time.Duration;

@Configuration
public class AwsConfig {

    private SnsClient snsClient;

    @Bean
    @Primary
    public SnsClient snsClient(@Value("${aws.region}") String awsRegion) {
        this.snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())  // Secure AWS Credentials
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .apiCallTimeout(Duration.ofSeconds(10))  // Timeout Config
                        .apiCallAttemptTimeout(Duration.ofSeconds(5))
                        .build())
                .build();
        return this.snsClient;
    }

    @PreDestroy
    public void shutdown(){
        if (this.snsClient != null) {
            this.snsClient.close();
            System.out.println("SnsClient closed successfully");
        }
    }
}

/*
public class AwsConfig {

    private SnsClient snsClient;

    @Bean
    public SnsClient snsClient() {
        this.snsClient = SnsClient.builder().
                region(Region.AF_SOUTH_1).
                build();
        return this.snsClient;
    }

    @PreDestroy
    public void shutdown(){
        if (this.snsClient != null) {
            this.snsClient.close();
            System.out.println("SnsClient closed successfully");
        }
    }
}*/
