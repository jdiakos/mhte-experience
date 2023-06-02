package com.iknowhow.mhte.projectsexperience.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${app.taxis.jwt.secret}")
    private String jwtSecret;


    public DecodedJWT decodedJWT() {
        return null;
    }
}
