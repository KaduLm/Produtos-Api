package com.carlos.produtosapi.produtos_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.enums.UserRole;

import com.carlos.produtosapi.produtos_api.exceptions.TokenValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private Users usuario;

    private static final String SECRET_VALIDO = "minha-chave-secreta-de-teste";
    private static final String SECRET_INVALIDO = "chave-errada";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", SECRET_VALIDO);

        usuario = new Users("carlos", "senha123", UserRole.ADMIN);
    }


    @Nested
    @DisplayName("generateToken()")
    class GenerateToken {

        @Test
        @DisplayName("Deve gerar token JWT com sucesso")
        void deveGerarTokenComSucesso() {
            String token = tokenService.generateToken(usuario);

            assertThat(token).isNotBlank();
        }

        @Test
        @DisplayName("Deve gerar token com subject igual ao login do usuário")
        void deveGerarTokenComSubjectCorreto() {
            String token = tokenService.generateToken(usuario);

            String subject = JWT.require(Algorithm.HMAC256(SECRET_VALIDO))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();

            assertThat(subject).isEqualTo("carlos");
        }

        @Test
        @DisplayName("Deve gerar token com issuer 'auth-api'")
        void deveGerarTokenComIssuerCorreto() {
            String token = tokenService.generateToken(usuario);

            String issuer = JWT.require(Algorithm.HMAC256(SECRET_VALIDO))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getIssuer();

            assertThat(issuer).isEqualTo("auth-api");
        }

        @Test
        @DisplayName("Deve gerar token com data de expiração futura")
        void deveGerarTokenComExpiracaoFutura() {
            String token = tokenService.generateToken(usuario);

            Instant expiracao = JWT.require(Algorithm.HMAC256(SECRET_VALIDO))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getExpiresAtAsInstant();

            assertThat(expiracao).isAfter(Instant.now());
        }
    }


    @Nested
    @DisplayName("validateToken()")
    class ValidateToken {

        @Test
        @DisplayName("Deve validar token e retornar o subject corretamente")
        void deveValidarTokenERetornarSubject() {
            String token = tokenService.generateToken(usuario);

            String subject = tokenService.validateToken(token);

            assertThat(subject).isEqualTo("carlos");
        }

        @Test
        @DisplayName("Deve lançar TokenValidationException para token assinado com secret diferente")
        void deveLancarExcecaoParaTokenComSecretDiferente() {
            String tokenInvalido = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject("carlos")
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .sign(Algorithm.HMAC256(SECRET_INVALIDO));

            assertThatThrownBy(() -> tokenService.validateToken(tokenInvalido))
                    .isInstanceOf(TokenValidationException.class)
                    .hasMessage("Token inválido ou mal formatado");
        }

        @Test
        @DisplayName("Deve lançar TokenValidationException para token expirado")
        void deveLancarExcecaoParaTokenExpirado() {
            String tokenExpirado = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject("carlos")
                    .withExpiresAt(Instant.now().minusSeconds(3600))
                    .sign(Algorithm.HMAC256(SECRET_VALIDO));

            assertThatThrownBy(() -> tokenService.validateToken(tokenExpirado))
                    .isInstanceOf(TokenValidationException.class)
                    .hasMessage("Token expirado");
        }

        @Test
        @DisplayName("Deve lançar TokenValidationException para token mal formatado")
        void deveLancarExcecaoParaTokenMalFormatado() {
            assertThatThrownBy(() -> tokenService.validateToken("token.invalido.qualquer"))
                    .isInstanceOf(TokenValidationException.class)
                    .hasMessage("Token inválido ou mal formatado");
        }

        @Test
        @DisplayName("Deve lançar TokenValidationException para token com issuer incorreto")
        void deveLancarExcecaoParaTokenComIssuerIncorreto() {
            String tokenIssuerErrado = JWT.create()
                    .withIssuer("outro-issuer")
                    .withSubject("carlos")
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .sign(Algorithm.HMAC256(SECRET_VALIDO));

            assertThatThrownBy(() -> tokenService.validateToken(tokenIssuerErrado))
                    .isInstanceOf(TokenValidationException.class)
                    .hasMessage("Token inválido ou mal formatado");
        }

        @Test
        @DisplayName("Deve lançar TokenValidationException para token em branco")
        void deveLancarExcecaoParaTokenEmBranco() {
            assertThatThrownBy(() -> tokenService.validateToken(""))
                    .isInstanceOf(TokenValidationException.class);
        }
    }
}