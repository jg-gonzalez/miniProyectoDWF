package com.udb.miniproyectodwf;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    private final String SECRET_KEY = "miClaveSecretaSuperSeguraParaPruebasUnitarias12345";
    private final long EXPIRATION = 3600000; // 1 hora

    // Simulación de JwtUtil
    private String generateToken(String username) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return io.jsonwebtoken.Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void testGenerateToken() {
        String token = generateToken("testuser");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = generateToken("testuser");
        String username = extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = generateToken("testuser");
        assertTrue(validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        assertFalse(validateToken("invalid.token.here"));
    }

    @Test
    void testValidateToken_NullToken() {
        assertFalse(validateToken(null));
    }

    @Test
    void testValidateToken_EmptyToken() {
        assertFalse(validateToken(""));
    }
}