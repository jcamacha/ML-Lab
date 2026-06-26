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

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    private User sampleUser;
    private DecodedJWT mockJwt;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("User One");
        sampleUser.setEmail("user.one@example.com");
        sampleUser.setRole("USER");

        mockJwt = mock(DecodedJWT.class);
    }

    @Test
    void getAllUsers_AsAdmin_Success() {
        String token = "Bearer admin_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("ADMIN");
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = userController.getAllUsers(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_AsUser_Forbidden() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");

        ResponseEntity<?> response = userController.getAllUsers(token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getAllUsers_NoToken_Unauthorized() {
        ResponseEntity<?> response = userController.getAllUsers(null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUserById_Self_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(userService.getUserById(1L)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> response = userController.getUserById(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleUser, response.getBody());
    }

    @Test
    void getUserById_OtherUser_Forbidden() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(2L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");

        ResponseEntity<?> response = userController.getUserById(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void createUser_Success() {
        when(userService.saveUser(any(User.class))).thenReturn(sampleUser);

        ResponseEntity<User> response = userController.createUser(sampleUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleUser, response.getBody());
    }

    @Test
    void updateUser_Self_Success() {
        String token = "Bearer user_token";
        User updateDetails = new User();
        updateDetails.setName("Updated Name");
        updateDetails.setRole("ADMIN"); // trying to escalate

        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(userService.updateUser(eq(1L), any(User.class))).thenAnswer(invocation -> invocation.getArgument(1));

        ResponseEntity<?> response = userController.updateUser(1L, updateDetails, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User responseUser = (User) response.getBody();
        assertNotNull(responseUser);
        assertEquals("Updated Name", responseUser.getName());
        assertEquals("USER", responseUser.getRole()); // role reset to USER
    }

    @Test
    void deleteUser_AsAdmin_Success() {
        String token = "Bearer admin_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("ADMIN");
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void login_Success() {
        UserController.LoginRequest request = new UserController.LoginRequest("user.one@example.com", "password");
        when(userService.authenticate("user.one@example.com", "password")).thenReturn(sampleUser);
        when(jwtService.generateToken(sampleUser)).thenReturn("sample_token");

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("sample_token", body.get("token"));
        assertEquals(1L, body.get("userId"));
    }

    @Test
    void login_Failure() {
        UserController.LoginRequest request = new UserController.LoginRequest("user.one@example.com", "wrong");
        when(userService.authenticate("user.one@example.com", "wrong")).thenReturn(null);

        ResponseEntity<?> response = userController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
