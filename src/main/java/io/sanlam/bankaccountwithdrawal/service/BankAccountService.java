package io.sanlam.bankaccountwithdrawal.service;

import io.github.resilience4j.retry.annotation.Retry;
import io.sanlam.bankaccountwithdrawal.contractorport.BankAccountPort;
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

/*
Proxies in Spring: What They Are and How They Work
A proxy in Spring is an object that acts as a substitute for another object. Proxies allow Spring to add additional behavior, such as transaction management, security, caching, lazy loading, and AOP (Aspect-Oriented Programming), without modifying the actual class.

Spring uses two main types of proxies:

JDK Dynamic Proxies → Used when an interface is present.
CGLIB Proxies → Used when there is no interface (works with concrete classes).
*/

@Service
public class BankAccountService {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    private final BankAccountPort bankAccountPort;
    private final EventPublisher eventPublisher;

    public BankAccountService(BankAccountPort bankAccountPort, EventPublisher eventPublisher) {
        this.bankAccountPort = bankAccountPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Retry(name = "databaseRetry", fallbackMethod = "fallbackResponse")
    public void withdraw(Long account_Id, BigDecimal amount) {

        // Fetch current balance
        Optional<BigDecimal> currentBalance = bankAccountPort.getBalance(account_Id);

        // Check if balance is present and compare it with the withdrawal amount
        if (currentBalance.isEmpty() || currentBalance.get().compareTo(amount) < 0) {
            publishEvent(account_Id, amount, "FAILED: Insufficient Funds");
            throw new InsufficientFundsException("Insufficient funds for account: " + account_Id);
        }

        // Update the account balance through the port
        boolean updated = bankAccountPort.updateBalance(account_Id, amount);

        if (updated) {
            publishEvent(account_Id, amount, "SUCCESSFUL");
        } else {
            publishEvent(account_Id, amount, "FAILED: Balance Update");
            throw new DatabaseUpdateException("Failed to update balance for account: " + account_Id);
        }
    }

    // Fallback method for retry
    public void fallbackResponse(Long account_Id, BigDecimal amount, Throwable t) {
        logger.error("Retry attempt failed for withdrawal: account_Id={}, amount={}", account_Id, amount, t);
        publishEvent(account_Id, amount, "FAILED: Retry Failed");
        throw new RuntimeException("Database is currently unavailable, please try again later.");
    }

    // Helper method for publishing events
    private void publishEvent(Long account_Id, BigDecimal amount, String status) {
        WithdrawalEvent event = new WithdrawalEvent(amount, account_Id, status,
                ZonedDateTime.now(ZoneId.of("Africa/Johannesburg")).toString());
        eventPublisher.publishEvent(event, "withdrawal-events");
    }
}