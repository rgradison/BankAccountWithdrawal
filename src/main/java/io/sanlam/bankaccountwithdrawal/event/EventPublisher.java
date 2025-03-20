package io.sanlam.bankaccountwithdrawal.event;

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

}