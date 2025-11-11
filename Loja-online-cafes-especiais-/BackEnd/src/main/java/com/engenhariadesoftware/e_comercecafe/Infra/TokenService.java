package com.engenhariadesoftware.e_comercecafe.Infra;

import java.time.Instant;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.engenhariadesoftware.e_comercecafe.Models.UsuarioModel;

@Service
public class TokenService {

    private String secret = "R@osemeire12";

    public String gerarToken(UsuarioModel usuario) {
    try {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
            .withIssuer("auth-e-comercecafe")
            .withSubject(usuario.getIdUsuario().toString())
            .withExpiresAt(dataExpiracao())
            .sign(algorithm);
            return token;
    } catch (Exception e) {
        throw new RuntimeException("Erro ao gerar token", e);
    }
    }


    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("auth-e-comercecafe")
                .build()
                .verify(token)
                .getSubject();
        } catch (Exception e) {
             throw new RuntimeException("Erro ao validar token", e);
        }
    }



    private Instant dataExpiracao() {
        return Instant.now().plus(Duration.ofHours(2));
    }
}
