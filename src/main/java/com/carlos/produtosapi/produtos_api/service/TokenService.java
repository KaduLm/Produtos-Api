package com.carlos.produtosapi.produtos_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.carlos.produtosapi.produtos_api.entity.Users;
import com.carlos.produtosapi.produtos_api.exceptions.TokenGenerationException;
import com.carlos.produtosapi.produtos_api.exceptions.TokenValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(Users users) {
        try {
            log.info("Gerando token para o usuário: {}", users.getLogin());
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(users.getLogin())
                    .withExpiresAt(generateExpirationDate())
                    .sign(Algorithm.HMAC256(secret));
            log.info("Token gerado com sucesso para: {}", users.getLogin());
            return token;
        } catch (JWTCreationException e) {
            log.error("Erro ao gerar token para o usuário: {}", users.getLogin(), e);
            throw new TokenGenerationException("Erro ao gerar token para o usuário: " + users.getLogin());
        }
    }


    public String validateToken(String token) {
        try {
            log.debug("Validando token");
            String subject = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
            log.debug("Token válido para o usuário: {}", subject);
            return subject;
        } catch (TokenExpiredException _) {
            log.warn("Token expirado");
            throw new TokenValidationException("Token expirado");
        } catch (JWTVerificationException e) {
            log.warn("Token inválido: {}", e.getMessage());
            throw new TokenValidationException("Token inválido ou mal formatado");
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
