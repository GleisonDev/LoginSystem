package com.sireostech.vstore.gestao.infrastructure.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sireostech.vstore.gestao.domain.model.IdentifiablePrincipal;
import com.sireostech.vstore.gestao.application.exception.JwtGenerationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private final String jwtSecret;

    private final Algorithm algorithm;

    private static final String CLAIM_ROLE = "role";
    private static final String ISSUER = "vstore-api";
    private static final long EXPIRATION_HOURS = 2;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;

        try {
            byte[] secretBytes = Base64.getDecoder().decode(this.jwtSecret);

            this.algorithm = Algorithm.HMAC256(secretBytes);

        } catch (IllegalArgumentException e) {
            throw new JwtGenerationException("A chave JWT secreta é inválida. Verifique se ela é Base64 válida.", e);
        }
    }

    public String generateToken(IdentifiablePrincipal principal) {
        Instant expirationTime = Instant.now().plus(EXPIRATION_HOURS, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(principal.getId().toString())
                .withClaim(CLAIM_ROLE, principal.getPerfil().toString())
                .withExpiresAt(expirationTime)
                .sign(algorithm);
    }

    public String validateTokenAndGetUserId(String token) {
        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();
    }

    public Duration getTokenExpirationRemaining(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationDate = jwt.getExpiresAt();

            if (expirationDate == null) {
                return Duration.ZERO;
            }

            Instant expirationInstant = expirationDate.toInstant();
            Instant now = Instant.now();

            if (expirationInstant.isAfter(now)) {

                return Duration.between(now, expirationInstant);
            }

            return Duration.ZERO;

        } catch (Exception e) {
            System.err.println("Erro ao calcular tempo restante do token: " + e.getMessage());
            return Duration.ZERO;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            return JWT.decode(token).getSubject();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("Não foi possível extrair o ID do token para o logoff.", e);
        }
    }
}
