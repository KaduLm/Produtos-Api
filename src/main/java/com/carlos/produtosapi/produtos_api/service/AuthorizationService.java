package com.carlos.produtosapi.produtos_api.service;

import com.carlos.produtosapi.produtos_api.dto.request.AuthenticationRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.request.RegisterRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.response.LoginResponseDTO;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.enums.UserRole;
import com.carlos.produtosapi.produtos_api.exceptions.UsuarioAlredyExistsException;
import com.carlos.produtosapi.produtos_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public void registrarUsuario(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByLogin(registerRequestDTO.username()).isPresent())
            throw new UsuarioAlredyExistsException("Usuário já existe no banco");

        Users entity = new Users(registerRequestDTO.username(), new BCryptPasswordEncoder().encode(registerRequestDTO.password()));
        userRepository.save(entity);
    }

    public LoginResponseDTO getLoginResponseDTO(AuthenticationRequestDTO authenticationRequestDTO) {
        System.out.println("Tentando logar com: " + authenticationRequestDTO);
        return new LoginResponseDTO(tokenService.generateToken((Users) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.username(),
                        authenticationRequestDTO.password()))
                .getPrincipal()));
    }


    public void atualizarRoleUsuario(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + id));

        user.setRole(UserRole.ADMIN);
        userRepository.save(user);
    }
}
