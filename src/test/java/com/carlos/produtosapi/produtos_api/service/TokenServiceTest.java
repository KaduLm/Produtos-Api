package com.carlos.produtosapi.produtos_api.service;

import com.carlos.produtosapi.produtos_api.dto.request.AuthenticationRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.request.RegisterRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.response.LoginResponseDTO;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.enums.UserRole;
import com.carlos.produtosapi.produtos_api.exceptions.UsuarioAlredyExistsException;
import com.carlos.produtosapi.produtos_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthorizationService authorizationService;

    private Users usuario;
    private RegisterRequestDTO registerRequestDTO;
    private AuthenticationRequestDTO authenticationRequestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Users("carlos", "senha123encodada");

        registerRequestDTO = new RegisterRequestDTO("carlos", "senha123");
        authenticationRequestDTO = new AuthenticationRequestDTO("carlos", "senha123");
    }


    @Nested
    @DisplayName("loadUserByUsername()")
    class LoadUserByUsername {

        @Test
        @DisplayName("Deve retornar UserDetails quando usuário existir")
        void deveRetornarUserDetailsQuandoUsuarioExistir() {
            when(userRepository.findByLogin("carlos")).thenReturn(Optional.of(usuario));

            UserDetails resultado = authorizationService.loadUserByUsername("carlos");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getUsername()).isEqualTo("carlos");
            verify(userRepository, times(1)).findByLogin("carlos");
        }

        @Test
        @DisplayName("Deve lançar UsernameNotFoundException quando usuário não existir")
        void deveLancarExcecaoQuandoUsuarioNaoExistir() {
            when(userRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authorizationService.loadUserByUsername("inexistente"))
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("Usuário não encontrado: inexistente");

            verify(userRepository, times(1)).findByLogin("inexistente");
        }
    }


    @Nested
    @DisplayName("registrarUsuario()")
    class RegistrarUsuario {

        @Test
        @DisplayName("Deve registrar usuário com sucesso quando login não existir")
        void deveRegistrarUsuarioComSucesso() {
            when(userRepository.findByLogin("carlos")).thenReturn(Optional.empty());

            authorizationService.registrarUsuario(registerRequestDTO);

            verify(userRepository, times(1)).findByLogin("carlos");
            verify(userRepository, times(1)).save(any(Users.class));
        }

        @Test
        @DisplayName("Deve salvar usuário com senha encodada (não em texto puro)")
        void deveSalvarUsuarioComSenhaEncodada() {
            when(userRepository.findByLogin("carlos")).thenReturn(Optional.empty());

            authorizationService.registrarUsuario(registerRequestDTO);

            ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
            verify(userRepository).save(captor.capture());

            Users usuarioSalvo = captor.getValue();
            assertThat(usuarioSalvo.getPassword())
                    .isNotEqualTo("senha123")
                    .startsWith("$2a$")
                    .hasSizeGreaterThan(20);
        }

        @Test
        @DisplayName("Deve salvar usuário com login e role corretos")
        void deveSalvarUsuarioComDadosCorretos() {
            when(userRepository.findByLogin("carlos")).thenReturn(Optional.empty());

            authorizationService.registrarUsuario(registerRequestDTO);

            ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
            verify(userRepository).save(captor.capture());

            Users usuarioSalvo = captor.getValue();
            assertThat(usuarioSalvo.getUsername()).isEqualTo("carlos");
            assertThat(usuarioSalvo.getRole()).isEqualTo(UserRole.USER);
        }

        @Test
        @DisplayName("Deve lançar UsuarioAlredyExistsException quando login já existir")
        void deveLancarExcecaoQuandoLoginJaExistir() {
            when(userRepository.findByLogin("carlos")).thenReturn(Optional.of(usuario));

            assertThatThrownBy(() -> authorizationService.registrarUsuario(registerRequestDTO))
                    .isInstanceOf(UsuarioAlredyExistsException.class)
                    .hasMessage("Usuário já existe no banco");

            verify(userRepository, never()).save(any());
        }
    }


    @Nested
    @DisplayName("getLoginResponseDTO()")
    class GetLoginResponseDTO {

        @Test
        @DisplayName("Deve retornar LoginResponseDTO com token ao autenticar com sucesso")
        void deveRetornarLoginResponseDTOComToken() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(usuario);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(tokenService.generateToken(usuario)).thenReturn("jwt-token-gerado");

            LoginResponseDTO resultado = authorizationService.getLoginResponseDTO(authenticationRequestDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.token()).isEqualTo("jwt-token-gerado");
        }

        @Test
        @DisplayName("Deve chamar AuthenticationManager com as credenciais corretas")
        void deveChamarAuthenticationManagerComCredenciaisCorretas() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(usuario);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(tokenService.generateToken(usuario)).thenReturn("jwt-token-gerado");

            authorizationService.getLoginResponseDTO(authenticationRequestDTO);

            ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                    ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
            verify(authenticationManager).authenticate(captor.capture());

            UsernamePasswordAuthenticationToken authToken = captor.getValue();
            assertThat(authToken.getPrincipal()).isEqualTo("carlos");
            assertThat(authToken.getCredentials()).isEqualTo("senha123");
        }

        @Test
        @DisplayName("Deve chamar TokenService com o usuário retornado pelo AuthenticationManager")
        void deveChamarTokenServiceComUsuarioAutenticado() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(usuario);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(tokenService.generateToken(usuario)).thenReturn("jwt-token-gerado");

            authorizationService.getLoginResponseDTO(authenticationRequestDTO);

            verify(tokenService, times(1)).generateToken(usuario);
        }

        @Test
        @DisplayName("Deve propagar BadCredentialsException quando credenciais forem inválidas")
        void devePropagrarExcecaoQuandoCredenciaisInvalidas() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            assertThatThrownBy(() -> authorizationService.getLoginResponseDTO(authenticationRequestDTO))
                    .isInstanceOf(BadCredentialsException.class);

            verifyNoInteractions(tokenService);
        }
    }
}