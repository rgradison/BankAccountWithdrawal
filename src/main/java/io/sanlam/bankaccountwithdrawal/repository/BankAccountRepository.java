package io.sanlam.bankaccountwithdrawal.repository;

import io.sanlam.bankaccountwithdrawal.exception.DatabaseUpdateException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class BankAccountRepository {

    private final JdbcTemplate jdbcTemplate;

    public BankAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BigDecimal> getBalance(Long accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // Return empty if no record found
        }
    }

    public boolean updateBalance(Long accountId, BigDecimal amount) {
        String sql = "update accounts set balance = balance - ? where id = ?";

        try {
            // Execute the update query and get the number of rows affected
            int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
            // If no rows were affected, throw an exception
            if (rowsAffected == 0) {
                throw new DatabaseUpdateException("Failed to update balance for account ID: " + accountId);
            }

            // Return true if the update was successful (i.e., at least one row was affected)
            return true;

        } catch (Exception e) {
            // If any other exception occurs, wrap it in a DatabaseUpdateException
            throw new DatabaseUpdateException("Error updating balance for account ID: " + accountId, e);
        }
    }

}