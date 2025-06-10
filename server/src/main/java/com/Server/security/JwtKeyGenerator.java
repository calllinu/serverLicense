package com.Server.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyGenerator {
    public SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
