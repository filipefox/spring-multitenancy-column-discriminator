package com.example.demo.core.services;

import com.example.demo.core.utils.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {

    @Value("${jwt.signing.key}")
    private String jwtSigningKey;
    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public String createJwt(String email, int roleId, String roleName, int tenantId) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(email)
                .claim(Constants.ROLE_ID_CLAIM, roleId)
                .claim(Constants.ROLE_NAME_CLAIM, roleName)
                .claim(Constants.TENANT_ID_CLAIM, tenantId)
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parseJwt(String authorizationHeader) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return jwtParser.parseClaimsJws(authorizationHeader.replace(Constants.BEARER_SCHEMA, ""));
    }

    public String createSuperAdminJwt(String email, int tenantId) {
        return createJwt(email, Constants.SUPER_ADMIN_ROLE_ID, Constants.SUPER_ADMIN_ROLE_NAME, tenantId);
    }
}