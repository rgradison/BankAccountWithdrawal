package io.sanlam.bankaccountwithdrawal.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.CompletableFuture;

@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String kafkaTopic;

    public EventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                          @Value("${kafka.topic.name}") String kafkaTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopic = kafkaTopic;
    }

    public void publishEvent(WithdrawalEvent event, String topic) {
        try {
            // Serialize event object to JSON
            String eventJson = new ObjectMapper().writeValueAsString(event);

            // Send event JSON to Kafka topic asynchronously
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, eventJson);

            future.whenComplete((sendResult, ex) -> {
                if (ex != null) {
                    // Log the error if sending the event to Kafka fails
                    logger.error("❌ Failed to send event to Kafka: {}", ex.getMessage(), ex);
                } else {
                    // Log the successful event publishing to Kafka
                    RecordMetadata metadata = sendResult.getRecordMetadata();
                    logger.info("✅ Successfully published event to Kafka: topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });
        } catch (Exception e) {
            logger.error("Error serializing event: {}", e.getMessage(), e);
        }
    }
}