package io.sanlam.bankaccountwithdrawal.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import io.sanlam.bankaccountwithdrawal.event.EventPublisher;
import io.sanlam.bankaccountwithdrawal.exception.DatabaseUpdateException;
import io.sanlam.bankaccountwithdrawal.exception.InsufficientFundsException;
import io.sanlam.bankaccountwithdrawal.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BankAccountService {

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
        BigDecimal currentBalance = bankAccountRepository.getBalance(accountId);

        // Insufficient funds
        if(currentBalance == null || currentBalance.compareTo(amount) < 0) {
            // Publish and throw event for insufficient funds
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "FAILED: Insufficient Funds");
            eventPublisher.publishEvent(event);
            throw new InsufficientFundsException("Insufficient funds for account: " + accountId);
        }

        // Update the account balance
        boolean updated = bankAccountRepository.updateBalance(accountId,amount);

        if (updated) {
            // Publish successful withdrawal event
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
            eventPublisher.publishEvent(event);
        } else {
            // Publish event for failed balance update (optional)
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "FAILED: Balance Update");
            eventPublisher.publishEvent(event);
            throw new DatabaseUpdateException("Failed to update balance for account: " + accountId);
        }

    }

    public void fallbackResponse(Exception e) {
        throw new RuntimeException("Database is currently unavailable, please try again later.");
    }

}