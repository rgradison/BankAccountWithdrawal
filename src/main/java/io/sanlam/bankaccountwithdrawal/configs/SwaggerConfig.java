package io.sanlam.bankaccountwithdrawal.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
info = @Info(
        title = "Bank Account Withdrawal",
        version = "1.0",
        description = "API documentation for Banking System"
        )
     )
public class SwaggerConfig {
}
