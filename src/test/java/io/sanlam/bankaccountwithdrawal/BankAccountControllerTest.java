package io.sanlam.bankaccountwithdrawal;

import io.sanlam.bankaccountwithdrawal.controller.BankAccountController;
import io.sanlam.bankaccountwithdrawal.exception.InsufficientFundsException;
import io.sanlam.bankaccountwithdrawal.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BankAccountController.class) // Load only the controller layer
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests

    @MockitoBean
    private BankAccountService bankAccountService; // Mock service layer

    @Test
    void withdraw_SuccessfulTransaction_Returns200() throws Exception {
        // Arrange (Mock successful withdrawal)
        doNothing().when(bankAccountService).withdraw(1L, new BigDecimal("100.00"));

        // Act & Assert
        mockMvc.perform(post("/bank/withdraw")
                        .param("accountId", "1")
                        .param("amount", "100.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(jsonPath("$.Message").value("Withdrawal successful")); // Validate response body
    }
    @Test
    void withdraw_InsufficientFunds_Returns400() throws Exception {
        // Arrange (Mock insufficient funds exception)
        doThrow(new InsufficientFundsException("Insufficient funds for account: 1"))
                .when(bankAccountService).withdraw(1L, new BigDecimal("5000.00"));

        // Act & Assert
        mockMvc.perform(post("/bank/withdraw")
                        .param("accountId", "1")
                        .param("amount", "5000.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expect HTTP 400 BAD REQUEST
                .andExpect(jsonPath("$.Error").value("Insufficient funds for account: 1"));
    }

    @Test
    void withdraw_UnexpectedError_Returns500() throws Exception {
        // Arrange (Mock unexpected exception)
        doThrow(new RuntimeException("Database connection failed"))
                .when(bankAccountService).withdraw(1L, new BigDecimal("50.00"));

        // Act & Assert
        mockMvc.perform(post("/bank/withdraw")
                        .param("accountId", "1")
                        .param("amount", "50.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()) // Expect HTTP 500 INTERNAL SERVER ERROR
                .andExpect(jsonPath("$.Error").value("An unexpected error occurred."));
    }

}
