package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.Experiment;
import org.example.services.ExperimentService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExperimentControllerTest {

    @Mock
    private ExperimentService experimentService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ExperimentController experimentController;

    private Experiment sampleExperiment;
    private DecodedJWT mockJwt;

    @BeforeEach
    void setUp() {
        sampleExperiment = new Experiment();
        sampleExperiment.setExperiment_id(1L);
        sampleExperiment.setModel_name("SVM");
        sampleExperiment.setDataset_name("Iris");
        sampleExperiment.setUserId(1L);
        sampleExperiment.setModelId(2L);

        mockJwt = mock(DecodedJWT.class);
    }

    @Test
    void saveExperiment_User_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(experimentService.saveExperiment(any(Experiment.class))).thenReturn(sampleExperiment);

        ResponseEntity<?> response = experimentController.saveExperiment(sampleExperiment, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(experimentService, times(1)).saveExperiment(sampleExperiment);
    }

    @Test
    void saveExperiment_Unauthorized() {
        ResponseEntity<?> response = experimentController.saveExperiment(sampleExperiment, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getAllExperiments_Admin_Success() {
        String token = "Bearer admin_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("ADMIN");
        when(experimentService.getAllExperiments()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = experimentController.getAllExperiments(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(experimentService, times(1)).getAllExperiments();
    }

    @Test
    void getAllExperiments_User_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(experimentService.getExperimentsByUserId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = experimentController.getAllExperiments(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(experimentService, times(1)).getExperimentsByUserId(1L);
    }

    @Test
    void getExperimentsByUserId_Self_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(experimentService.getExperimentsByUserId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = experimentController.getExperimentsByUserId(1L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getExperimentsByUserId_Other_Forbidden() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(2L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");

        ResponseEntity<?> response = experimentController.getExperimentsByUserId(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getExperimentsByModelId_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(experimentService.getExperimentsByModelId(2L)).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = experimentController.getExperimentsByModelId(2L, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteExperiment_Creator_Success() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(1L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(experimentService.getExperimentById(1L)).thenReturn(Optional.of(sampleExperiment));
        doNothing().when(experimentService).deleteExperiment(1L);

        ResponseEntity<?> response = experimentController.deleteExperiment(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteExperiment_Other_Forbidden() {
        String token = "Bearer user_token";
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getUserIdFromToken(mockJwt)).thenReturn(2L);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");
        when(experimentService.getExperimentById(1L)).thenReturn(Optional.of(sampleExperiment));

        ResponseEntity<?> response = experimentController.deleteExperiment(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
