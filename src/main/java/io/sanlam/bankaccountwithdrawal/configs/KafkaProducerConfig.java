package io.sanlam.bankaccountwithdrawal.configs;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfig.class);

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.producer.retries}")
    private int retries;

    @Value("${kafka.producer.batch-size}")
    private int batchSize;

    @Value("${kafka.producer.linger-ms}")
    private int lingerMs;

    @Bean
    public ProducerFactory<String, String> producerFactory() {

        logger.info("Kafka Bootstrap Servers: {}", kafkaBootstrapServers); // Using SLF4J for logging

        if (kafkaBootstrapServers == null || kafkaBootstrapServers.isEmpty()) {
            throw new IllegalArgumentException("Kafka Bootstrap Servers cannot be null or empty");
        }

        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Optional: Customize retry, batching, or other production settings
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, retries);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        config.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}