package com.iknowhow.mhte.projectsexperience.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.taxis.jwt.secret}")
    private String jwtSecret;

    @Value("${app.taxis.jwt.issuer}")
    private String jwtIssuer;

    public String getUsernameFromJwtToken(String jwtToken) {

        return decodedJWT(jwtToken).getSubject();
    }

    public boolean validateJwtToken(String jwtToken) {

        return decodedJWT(jwtToken) != null;
    }


    public DecodedJWT decodedJWT(String jwtToken) {
        DecodedJWT decodedJWT = null;
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build();
            decodedJWT = verifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }

        return decodedJWT;
    }
}
