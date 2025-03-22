package io.sanlam.bankaccountwithdrawal.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheck {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // This is the stored hashed password
        String storedHashedPassword = "$2a$10$kj62f9xc.KqCpj5z9mEO7.XbURICoo.ccxyipNdKojWlYfelHWez6";

        // Try checking against a password
        String inputPassword = "password123"; // Try the password you expect

        boolean matches = encoder.matches(inputPassword, storedHashedPassword);
        System.out.println("Password matches: " + matches);
    }
}