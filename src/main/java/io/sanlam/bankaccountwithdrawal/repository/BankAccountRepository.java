package io.sanlam.bankaccountwithdrawal.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getBalance(Long accountId) {
        String sql = "select balance from account where id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);
    }

    public void updateBalance(Long accountId, BigDecimal amount) {
        String sql = "update accounts set balance = balance - ? where id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }
}