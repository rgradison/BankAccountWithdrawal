package io.sanlam.bankaccountwithdrawal.service;
//import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WithdrawalEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawalEventConsumer.class);

    // Jackson ObjectMapper for deserialization
    private final ObjectMapper objectMapper;

    // Constructor ObjectMapper dependency injection
    public WithdrawalEventConsumer(/*EventStoreDBClient eventStoreDBClient,*/ ObjectMapper objectMapper) {
        logger.debug("WithdrawalEventConsumer constructor called.");
        this.objectMapper = objectMapper;
    }

    // Kafka listener for consuming withdrawal events
    @KafkaListener(topics = "withdrawal-events", groupId = "withdrawal-group")
    public void consume(ConsumerRecord<String, String> record) {

        logger.info("Received event: Key={}, Value={}", record.key(), record.value());

        try {
            // Deserialize JSON into WithdrawalEvent
            WithdrawalEvent event = objectMapper.readValue(record.value(), WithdrawalEvent.class);
            logger.info("Event deserialized: {}", event);

        } catch (SerializationException e) {
            logger.error("Failed to deserialize event: {}", record.value(), e);
        } catch (Exception e) {
            logger.error("Error processing event: {}", record.value(), e);
        }
    }

    // Deserialize the JSON string to a WithdrawalEvent object
    private WithdrawalEvent deserializeEvent(String json) {
        try {
            return objectMapper.readValue(json, WithdrawalEvent.class);
        } catch (Exception e) {
            logger.error("Failed to deserialize event: {}", e.getMessage());
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}