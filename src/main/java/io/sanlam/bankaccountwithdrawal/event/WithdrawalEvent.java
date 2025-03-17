package io.sanlam.bankaccountwithdrawal.event;

import java.math.BigDecimal;

public class WithdrawalEvent {
    private final BigDecimal amount;
    private final long accountId;
    private final String status;

    public WithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public long getAccountId() {
        return accountId;
    }
    public String getStatus() {
        return status;
    }

    public String toJson(){
        return String.format("{\"amount\":\"%s\",\"accountId\":\"%d\",\"status\":\"%s\"}",
                amount.toString(), accountId, status);
    }
}