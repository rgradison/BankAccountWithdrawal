package io.sanlam.bankaccountwithdrawal.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import io.sanlam.bankaccountwithdrawal.event.EventPublisher;
import io.sanlam.bankaccountwithdrawal.exception.DatabaseUpdateException;
import io.sanlam.bankaccountwithdrawal.exception.InsufficientFundsException;
import io.sanlam.bankaccountwithdrawal.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private final BankAccountRepository bankAccountRepository;
    private final EventPublisher eventPublisher;

    public BankAccountService(BankAccountRepository bankAccountRepository, EventPublisher eventPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Retry(name = "databaseRetry", fallbackMethod = "fallbackResponse")
    public  void withdraw(Long accountId, BigDecimal amount) {

        // Fetch current balance
        Optional<BigDecimal> currentBalance = bankAccountRepository.getBalance(accountId);

        // Insufficient funds
        if(currentBalance.isEmpty() || currentBalance.get().compareTo(amount) < 0) {
            // Publish and throw event for insufficient funds
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "FAILED: Insufficient Funds", ZonedDateTime.now(ZoneId.of("Africa/Johannesburg")).toString());
            eventPublisher.publishEvent(event,"withdrawal-events");
            throw new InsufficientFundsException("Insufficient funds for account: " + accountId);
        }

        // Update the account balance
        boolean updated = bankAccountRepository.updateBalance(accountId,amount);

        if (updated) {
            // Publish successful withdrawal event
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL", ZonedDateTime.now(ZoneId.of("Africa/Johannesburg")).toString());
            eventPublisher.publishEvent(event,"withdrawal-events");
        } else {
            // Publish event for failed balance update (optional)
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "FAILED: Balance Update", ZonedDateTime.now(ZoneId.of("Africa/Johannesburg")).toString());
            eventPublisher.publishEvent(event,"withdrawal-events");
            throw new DatabaseUpdateException("Failed to update balance for account: " + accountId);
        }

    }

    // Fallback method for retry
    public void fallbackResponse(Long accountId, BigDecimal amount, Throwable t) {
        // Handle retry fallback logic
        logger.error("Retry attempt failed for withdrawal: accountId={}, amount={}", accountId, amount, t);
        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "FAILED: Retry Failed", ZonedDateTime.now(ZoneId.of("Africa/Johannesburg")).toString());
        eventPublisher.publishEvent(event, "withdrawal-events"); // Publish to Kafka
        throw new RuntimeException("Database is currently unavailable, please try again later.");
    }

}