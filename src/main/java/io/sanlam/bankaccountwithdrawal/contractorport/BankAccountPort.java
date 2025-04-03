package io.sanlam.bankaccountwithdrawal.contractorport;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

/*Yes, Hexagonal Architecture (also known as Ports and Adapters architecture) can be incredibly helpful in decoupling
components, reducing dependencies, and improving the overall design of your system.
Let's dive into how it works and how it addresses the concerns you've raised.*/

//This port is like a socket—it defines what should be done but not how.
public interface BankAccountPort {
    Optional<BigDecimal> getBalance(@NotNull Long account_Id);
    boolean updateBalance(@NotNull Long account_Id, @NotNull BigDecimal amount);
}