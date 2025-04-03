package io.sanlam.bankaccountwithdrawal.adaptor;

import io.sanlam.bankaccountwithdrawal.contractorport.BankAccountPort;
import io.sanlam.bankaccountwithdrawal.repository.BankAccountRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class MySQLBankAccountAdapter implements BankAccountPort {

    private static final Logger logger = LoggerFactory.getLogger(MySQLBankAccountAdapter.class);

    private final BankAccountRepository bankAccountRepository;

    public MySQLBankAccountAdapter(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Optional<BigDecimal> getBalance(Long account_Id) {
        return (bankAccountRepository.getBalance(account_Id));
                //.orElseThrow(() -> new RuntimeException("Account not found")));
    }

    @Override
    public boolean updateBalance(Long account_Id, BigDecimal amount) {
        return bankAccountRepository.updateBalance(account_Id, amount);
    }
}