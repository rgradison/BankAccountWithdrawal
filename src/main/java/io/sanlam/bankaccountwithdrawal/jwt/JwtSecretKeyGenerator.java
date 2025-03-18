package io.sanlam.bankaccountwithdrawal.jwt;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static String generateSecretKey() {
        // Generate a 256-bit (32-byte) key using a secure random number generator
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(keyBytes);

        // Encode the key as a Base64 string to be used in your Spring Boot application
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Generated JWT Secret Key: " + secretKey);
    }

}
