package io.sanlam.bankaccountwithdrawal.controller;

import io.sanlam.bankaccountwithdrawal.service.BankAccountService;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    private static final Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

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