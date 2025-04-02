package io.sanlam.bankaccountwithdrawal.contractorport;

import java.math.BigDecimal;
import java.util.Optional;

//This port is like a socket—it defines what should be done but not how.
public interface BankAccountPort {
    Optional<BigDecimal> getBalance(Long account_Id);
    boolean updateBalance(Long account_Id, BigDecimal amount);
}