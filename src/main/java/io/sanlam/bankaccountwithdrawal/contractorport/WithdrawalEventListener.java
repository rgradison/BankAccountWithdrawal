package io.sanlam.bankaccountwithdrawal.contractorport;

import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
/*
✅ Decoupling: The event consumer implementation can be changed without modifying the rest of the system.
✅ Testing & Mocking: Easier to mock in unit tests using Mockito or Spring Boot Test.
✅ Extensibility: You can create multiple implementations (e.g., MockWithdrawalEventConsumer, AuditWithdrawalEventConsumer).
*/
public interface WithdrawalEventListener {
    void consume(ConsumerRecord<String, String> record);
    WithdrawalEvent deserializeEvent(String json);
}