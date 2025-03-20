package io.sanlam.bankaccountwithdrawal.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!"testUser".equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(username)
                .password("$2a$10$kj62f9xc.KqCpj5z9mEO7.XbURICoo.ccxyipNdKojWlYfelHWez6") // Hashed password
                .roles("USER")
                .build();
    }

}