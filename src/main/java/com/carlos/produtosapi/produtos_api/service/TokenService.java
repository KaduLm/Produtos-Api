package com.carlos.produtosapi.produtos_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.carlos.produtosapi.produtos_api.entity.Users;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Users users){
        try {
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(users.getLogin())
                    .withExpiresAt(generateExpirationDate())
                    .sign(Algorithm.HMAC256(secret));
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro ao gerar o token ", e);
        }
    }

    public String validateToken(String token){
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
