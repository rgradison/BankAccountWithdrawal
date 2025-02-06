package io.sanlam.bankaccountwithdrawal.service;

import io.sanlam.bankaccountwithdrawal.event.WithdrawalEvent;
import io.sanlam.bankaccountwithdrawal.event.EventPublisher;
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
    public  void withdraw(Long accountId, BigDecimal amount) {

        // Fetch current balance
        BigDecimal currentBalance = bankAccountRepository.getBalance(accountId);

        // Insufficient funds
        if(currentBalance == null || currentBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for account: " + accountId);
        }

        // Update the account balance
        bankAccountRepository.updateBalance(accountId,amount);
        WithdrawalEvent event = new WithdrawalEvent(amount,accountId,"SUCCESSFUL");
        eventPublisher.publishEvent(event);

    }
}