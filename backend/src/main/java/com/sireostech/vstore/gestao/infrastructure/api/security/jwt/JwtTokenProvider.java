package com.sireostech.vstore.gestao.infrastructure.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenProvider {

    // CHAVE SECRETA: Idealmente injetada via application.yml ou .properties
    @Value("${jwt.secret:minha_chave_secreta_padrao_para_dev}")
    private String jwtSecret;

    private static final String CLAIM_ROLE = "role";
    private static final String ISSUER = "vstore-api";

    /**
     * Gera um token JWT, incluindo o perfil (role) do usuário.
     */
    public String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        // Token expira em 2 horas
        Instant expirationTime = Instant.now().plus(2, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getId().toString()) // ID do usuário como Subject
                .withClaim(CLAIM_ROLE, usuario.getPerfil().toString())
                .withExpiresAt(expirationTime)
                .sign(algorithm);
    }

    /**
     * Valida o token e retorna o ID do usuário (Subject).
     */
    public String validateTokenAndGetUserId(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject(); // Retorna o ID do usuário (String)
    }
}