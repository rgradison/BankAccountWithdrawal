package io.sanlam.bankaccountwithdrawal.configs;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
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
}