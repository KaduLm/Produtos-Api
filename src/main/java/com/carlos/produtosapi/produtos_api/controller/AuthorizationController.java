package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.request.AuthenticationRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.response.LoginResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.request.RegisterRequestDTO;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.repository.UserRepository;
import com.carlos.produtosapi.produtos_api.service.TokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
    public ResponseEntity<LoginResponseDTO> login (@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(new LoginResponseDTO(tokenService.generateToken((Users) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.username(),
                        authenticationRequestDTO.password()))
                .getPrincipal())));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByLogin(registerRequestDTO.username()).isPresent())
            return ResponseEntity.badRequest().build();

        Users entity = new Users(registerRequestDTO.username(), new BCryptPasswordEncoder().encode(registerRequestDTO.password()), registerRequestDTO.role());
        userRepository.save(entity);
        return ResponseEntity.ok().build();
    }


}
