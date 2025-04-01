package io.sanlam.bankaccountwithdrawal;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.reactive.function.client.WebClient;*/

@SpringBootApplication
/*@EnableDiscoveryClient*/
public class BankAccountWithdrawalApplication {
    /*@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }*/

   /* @Bean
    public WebClient.Builder getWebClientBuilder(){
        return WebClient.builder();
    }*/

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Load .env file
        // Set environment variables programmatically
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(BankAccountWithdrawalApplication.class, args);
    }
}