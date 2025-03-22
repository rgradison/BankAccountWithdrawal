package io.sanlam.bankaccountwithdrawal.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;  // Injecting the value of KAFKA_BOOTSTRAP_SERVERS

    @Value("${kafka.producer.retries}")
    private int retries;

    @Value("${kafka.producer.batch-size}")
    private int batchSize;

    @Value("${kafka.producer.linger-ms}")
    private int lingerMs;

    @Bean
    public ProducerFactory<String, String> producerFactory() {

        System.out.println("Kafka Bootstrap Servers: " + kafkaBootstrapServers); // Debugging line

        if (kafkaBootstrapServers == null || kafkaBootstrapServers.isEmpty()) {
            throw new IllegalArgumentException("Kafka Bootstrap Servers cannot be null or empty");
        }

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers); // Read from ENV variable
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Optional: Customize retry, batching, or other production settings
        config.put(ProducerConfig.RETRIES_CONFIG, retries); // Retry 3 times on failure
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize); // 16 KB batch size
        config.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs); // Wait for 5ms before sending messages to Kafka

        // Optional: Add security configurations for production (SSL, SASL, etc.)

        return new DefaultKafkaProducerFactory<>(config);

    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.regions.Region;

import javax.annotation.PreDestroy;

@Configuration
public class AwsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);

    private SnsClient snsClient;

    */
/**
     * Create and configure an SNS client. This method returns a singleton instance of SnsClient.
     *//*

    @Bean
    public SnsClient snsClient() {
        if (snsClient == null) {
            // Ensure only one instance of snsClient is created
            snsClient = SnsClient.builder()
                    .region(Region.of(System.getenv("AWS_REGION")))  // Use environment variable for flexibility
                    .build();
            logger.info("SnsClient created successfully.");
        }
        return snsClient;
    }

    */
/**
     * Gracefully shutdown the SNS client during application termination.
     * This ensures that resources are freed and connections are closed.
     *//*

    @PreDestroy
    public void shutdown() {
        if (snsClient != null) {
            try {
                snsClient.close();
                logger.info("SnsClient closed successfully.");
            } catch (Exception e) {
                logger.error("Error closing SnsClient: ", e);
            }
        }
    }
}*/
