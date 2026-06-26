package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.services.*;
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
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock private UserService userService;
    @Mock private DatasetService datasetService;
    @Mock private ModelService modelService;
    @Mock private ExperimentService experimentService;
    @Mock private UploadedFileService uploadedFileService;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AdminController adminController;

    private final String adminToken = "Bearer admin_token";
    private final String userToken = "Bearer user_token";
    private DecodedJWT adminJwt;
    private DecodedJWT userJwt;

    @BeforeEach
    void setUp() {
        adminJwt = mock(DecodedJWT.class);
        userJwt = mock(DecodedJWT.class);
    }

    private void setupAdmin(String token, DecodedJWT jwt) {
        when(jwtService.verifyToken(token)).thenReturn(jwt);
        when(jwtService.getRoleFromToken(jwt)).thenReturn("ADMIN");
    }

    private void setupUser(String token, DecodedJWT jwt) {
        when(jwtService.verifyToken(token)).thenReturn(jwt);
        when(jwtService.getRoleFromToken(jwt)).thenReturn("USER");
    }

    @Test
    void getAllUsers_AsAdmin_Success() {
        setupAdmin(adminToken, adminJwt);
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = adminController.getAllUsers(adminToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_AsUser_Forbidden() {
        setupUser(userToken, userJwt);

        ResponseEntity<?> response = adminController.getAllUsers(userToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Acceso denegado. Se requieren permisos de administrador.", response.getBody());
    }

    @Test
    void deleteUser_AsAdmin_Success() {
        setupAdmin(adminToken, adminJwt);
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = adminController.deleteUser(1L, adminToken);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_AsAdmin_NotFound() {
        setupAdmin(adminToken, adminJwt);
        doThrow(new RuntimeException("Not Found")).when(userService).deleteUser(99L);

        ResponseEntity<?> response = adminController.deleteUser(99L, adminToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteDataset_AsAdmin_Success() {
        setupAdmin(adminToken, adminJwt);
        doNothing().when(datasetService).deleteDataset(1L);

        ResponseEntity<?> response = adminController.deleteDataset(1L, adminToken);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(datasetService, times(1)).deleteDataset(1L);
    }

    @Test
    void deleteModel_AsAdmin_Success() {
        setupAdmin(adminToken, adminJwt);
        doNothing().when(modelService).deleteModel(1L);

        ResponseEntity<?> response = adminController.deleteModel(1L, adminToken);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(modelService, times(1)).deleteModel(1L);
    }

    @Test
    void getStats_AsAdmin_Success() {
        setupAdmin(adminToken, adminJwt);
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        when(datasetService.getAllDatasets()).thenReturn(Collections.emptyList());
        when(modelService.getAllModels()).thenReturn(Collections.emptyList());
        when(experimentService.getAllExperiments()).thenReturn(Collections.emptyList());
        when(uploadedFileService.getAllFiles()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = adminController.getStats(adminToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> stats = (Map<?, ?>) response.getBody();
        assertEquals(0, stats.get("totalUsers"));
        assertEquals(0, stats.get("totalDatasets"));
    }
}
