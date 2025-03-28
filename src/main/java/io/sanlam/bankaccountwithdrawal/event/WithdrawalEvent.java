package io.sanlam.bankaccountwithdrawal.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class WithdrawalEvent {

    private final BigDecimal amount;
    private final long accountId;
    private final String status;
    private final String eventTime; // Timestamp field

    @JsonCreator
    public WithdrawalEvent(
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("accountId") long accountId,
            @JsonProperty("status") String status,
            @JsonProperty("eventTime") String eventTime) {
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
        this.eventTime = eventTime;
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

    public String getEventTime() {
        return eventTime;
    }

    // Convert event to JSON format
    public String toJson() {
        return String.format("{\"amount\":\"%s\",\"accountId\":\"%d\",\"status\":\"%s\",\"eventTime\":\"%s\"}",
                amount.toString(), accountId, status, eventTime);
    }
}