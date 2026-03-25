package com.carlos.produtosapi.produtos_api.config;

import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.enums.UserRole;
import com.carlos.produtosapi.produtos_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@exemplo.com";

        if (userRepository.findByLogin(adminEmail).isEmpty()) {
            Users admin = new Users();
            admin.setLogin(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
        }
    }
}