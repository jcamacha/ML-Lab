package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.Model;
import org.example.services.ModelService;
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
public class ModelControllerTest {

    @Mock
    private ModelService modelService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ModelController modelController;

    private Model sampleModel;

    @BeforeEach
    void setUp() {
        sampleModel = new Model();
        sampleModel.setModelId(1L);
        sampleModel.setName("Linear Regression");
        sampleModel.setCategory("Regression");
        sampleModel.setDescription("Simple linear regression model");
    }

    @Test
    void getAllModels_Success() {
        List<Model> models = new ArrayList<>();
        models.add(sampleModel);
        when(modelService.getAllModels()).thenReturn(models);

        ResponseEntity<List<Model>> response = modelController.getAllModels();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(modelService, times(1)).getAllModels();
    }

    @Test
    void getModelById_Success() {
        when(modelService.getModelById(1L)).thenReturn(Optional.of(sampleModel));

        ResponseEntity<Model> response = modelController.getModelById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
    }

    @Test
    void getModelById_NotFound() {
        when(modelService.getModelById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Model> response = modelController.getModelById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createModel_Success() {
        when(modelService.saveModel(any(Model.class))).thenReturn(sampleModel);

        ResponseEntity<Model> response = modelController.createModel(sampleModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
    }

    @Test
    void updateModel_Success() {
        when(modelService.updateModel(eq(1L), any(Model.class))).thenReturn(sampleModel);

        ResponseEntity<Model> response = modelController.updateModel(1L, sampleModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleModel, response.getBody());
    }

    @Test
    void updateModel_NotFound() {
        when(modelService.updateModel(eq(99L), any(Model.class))).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<Model> response = modelController.updateModel(99L, sampleModel);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteModel_AsAdmin_Success() {
        String token = "Bearer admin_token";
        DecodedJWT mockJwt = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("ADMIN");
        doNothing().when(modelService).deleteModel(1L);

        ResponseEntity<?> response = modelController.deleteModel(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteModel_AsUser_Forbidden() {
        String token = "Bearer user_token";
        DecodedJWT mockJwt = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");

        ResponseEntity<?> response = modelController.deleteModel(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
