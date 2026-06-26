package org.example.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        sampleUser = new User();
        sampleUser.setUserId(123L);
        sampleUser.setName("Alice");
        sampleUser.setEmail("alice@example.com");
        sampleUser.setRole("ADMIN");
    }

    @Test
    void generateAndVerifyToken_Success() {
        String token = jwtService.generateToken(sampleUser);
        assertNotNull(token);

        DecodedJWT decoded = jwtService.verifyToken(token);
        assertNotNull(decoded);

        assertEquals("alice@example.com", jwtService.getEmailFromToken(decoded));
        assertEquals(123L, jwtService.getUserIdFromToken(decoded));
        assertEquals("ADMIN", jwtService.getRoleFromToken(decoded));
    }

    @Test
    void verifyToken_WithBearerPrefix_Success() {
        String token = jwtService.generateToken(sampleUser);
        String bearerToken = "Bearer " + token;

        DecodedJWT decoded = jwtService.verifyToken(bearerToken);
        assertNotNull(decoded);
        assertEquals("alice@example.com", jwtService.getEmailFromToken(decoded));
    }

    @Test
    void verifyToken_InvalidToken_ReturnsNull() {
        DecodedJWT decoded = jwtService.verifyToken("invalid.token.value");
        assertNull(decoded);
    }

    @Test
    void verifyToken_NullOrBlank_ReturnsNull() {
        assertNull(jwtService.verifyToken(null));
        assertNull(jwtService.verifyToken("   "));
    }

    @Test
    void getEmailFromToken_NullJwt_ReturnsNull() {
        assertNull(jwtService.getEmailFromToken(null));
        assertNull(jwtService.getUserIdFromToken(null));
        assertNull(jwtService.getRoleFromToken(null));
    }
}
