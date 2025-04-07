package io.sanlam.bankaccountwithdrawal.controller;

import io.sanlam.bankaccountwithdrawal.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bank")
@Tag(name = "Banking Controller", description = "Handles banking transactions")
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Withdraw money from the account.")
    @ApiResponse(responseCode = "201",description = "Money withdrawn successfully.")
    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestParam("accountId") Long accountId,
                                                        @RequestParam("amount") BigDecimal amount) {

        logger.info("Initiating withdrawal for account: {}, amount: {}", accountId, amount);
        Map<String, Object> response = new HashMap<>();

        bankAccountService.withdraw(accountId, amount);

        response.put("Message", "Withdrawal successful");

        return ResponseEntity.ok(response);

    }
}