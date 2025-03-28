package io.sanlam.bankaccountwithdrawal.configs;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);
    private SnsClient snsClient;

    @Value("${aws.region}") // Allows overriding via properties
    private String awsRegion;

    @Bean
    @Lazy // Optimized: SnsClient is only initialized when needed
    public SnsClient snsClient() {
        if (snsClient == null) {
            snsClient = SnsClient.builder()
                    .region(Region.of(awsRegion)) // Allows dynamic region configuration
                    .build();
            logger.info("SnsClient initialized successfully in region: {}", awsRegion);
        }
        return snsClient;
    }

    @PreDestroy
    public void shutdown() {
        if (snsClient != null) {
            snsClient.close();
            logger.info("SnsClient closed successfully.");
        }
    }
}