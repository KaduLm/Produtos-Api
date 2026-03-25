package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.AuthenticationDTO;
import com.carlos.produtosapi.produtos_api.dto.LoginResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.RegisterDTO;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.repository.UserRepository;
import com.carlos.produtosapi.produtos_api.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody AuthenticationDTO authenticationDTO){
        return ResponseEntity.ok(new LoginResponseDTO(tokenService.generateToken((Users) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationDTO.username(),
                        authenticationDTO.password()))
                .getPrincipal())));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        if (userRepository.findByLogin(registerDTO.username()).isPresent())
            return ResponseEntity.badRequest().build();

        Users entity = new Users(registerDTO.username(), new BCryptPasswordEncoder().encode(registerDTO.password()), registerDTO.role());
        userRepository.save(entity);
        return ResponseEntity.ok().build();
    }


}
