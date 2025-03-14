package io.sanlam.bankaccountwithdrawal;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankAccountWithdrawalApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Load .env file
        // Set environment variables programmatically
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(BankAccountWithdrawalApplication.class, args);
    }
}