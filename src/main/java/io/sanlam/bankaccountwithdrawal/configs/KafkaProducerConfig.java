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

// Replaces sns
/*import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
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
}*/