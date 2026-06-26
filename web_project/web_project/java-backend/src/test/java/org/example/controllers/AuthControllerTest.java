package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.User;
import org.example.services.UserService;
import org.example.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("John Doe");
        sampleUser.setEmail("john.doe@example.com");
        sampleUser.setPasswordHash("hashed_password");
        sampleUser.setRole("USER");
    }

    @Test
    void register_Success() {
        when(userService.register(any(User.class))).thenReturn(sampleUser);

        ResponseEntity<?> response = authController.register(sampleUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleUser, response.getBody());
        verify(userService, times(1)).register(sampleUser);
    }

    @Test
    void register_Failure() {
        when(userService.register(any(User.class))).thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<?> response = authController.register(sampleUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Email already exists", body.get("error"));
    }

    @Test
    void login_Success() {
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("john.doe@example.com", "password");
        when(userService.authenticate("john.doe@example.com", "password")).thenReturn(sampleUser);
        when(jwtService.generateToken(sampleUser)).thenReturn("sample_token");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("sample_token", body.get("token"));
        assertNotNull(body.get("user"));
    }

    @Test
    void login_Failure() {
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest("john.doe@example.com", "wrong_password");
        when(userService.authenticate("john.doe@example.com", "wrong_password")).thenReturn(null);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Credenciales inválidas. Correo o contraseña incorrectos.", body.get("error"));
    }

    @Test
    void getMe_Success() {
        String token = "Bearer sample_token";
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(decodedJWT);
        when(jwtService.getEmailFromToken(decodedJWT)).thenReturn("john.doe@example.com");
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = authController.getMe(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(1L, body.get("userId"));
        assertEquals("john.doe@example.com", body.get("email"));
    }

    @Test
    void getMe_NoHeader() {
        ResponseEntity<?> response = authController.getMe(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Token no proporcionado", body.get("error"));
    }

    @Test
    void getMe_InvalidToken() {
        String token = "Bearer invalid_token";
        when(jwtService.verifyToken(token)).thenReturn(null);

        ResponseEntity<?> response = authController.getMe(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Token inválido o expirado", body.get("error"));
    }

    @Test
    void getMe_UserNotFound() {
        String token = "Bearer sample_token";
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(decodedJWT);
        when(jwtService.getEmailFromToken(decodedJWT)).thenReturn("john.doe@example.com");
        when(userService.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.getMe(token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Usuario no encontrado", body.get("error"));
    }
}
