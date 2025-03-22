package io.sanlam.bankaccountwithdrawal.event;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
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

    public void publishEvent(WithdrawalEvent event) {
        String eventJson = event.toJson();  // Convert event to JSON

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(kafkaTopic, eventJson);

        future.whenComplete((sendResult, ex) -> {
            if (ex != null) {
                logger.error("❌ Failed to send event to Kafka: {}", ex.getMessage(), ex);
            } else {
                RecordMetadata metadata = sendResult.getRecordMetadata();
                logger.info("✅ Successfully published event to Kafka: topic={}, partition={}, offset={}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}

/*
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;


@Component
public class EventPublisher {

    private final SnsClient snsClient;
    public final String snsTopicArn;

    public EventPublisher(SnsClient snsClient, @Value("${aws.sns.topicArn}") String snsTopicArn) {
        this.snsClient = snsClient;
        this.snsTopicArn = snsTopicArn;
    }

    public void publishEvent(WithdrawalEvent event) {
        String eventJson = event.toJson();
        PublishRequest request = PublishRequest.builder().
                                 message(eventJson).
                                 topicArn(snsTopicArn).
                                 build();
        snsClient.publish(request);

    }
}*/
