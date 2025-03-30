package io.sanlam.bankaccountwithdrawal.contract;

import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface WithdrawalEventListener {
    public void consume(ConsumerRecord<String, String> record);
    public WithdrawalEvent deserializeEvent(String json);
}