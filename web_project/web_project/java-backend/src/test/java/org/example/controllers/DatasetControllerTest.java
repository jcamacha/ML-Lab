package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.Dataset;
import org.example.services.DatasetService;
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
public class DatasetControllerTest {

    @Mock
    private DatasetService datasetService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private DatasetController datasetController;

    private Dataset sampleDataset;

    @BeforeEach
    void setUp() {
        sampleDataset = new Dataset();
        sampleDataset.setDatasetId(1L);
        sampleDataset.setName("Iris");
        sampleDataset.setDatasetType("Classification");
    }

    @Test
    void getDatasets_NoFilter_Success() {
        List<Dataset> datasets = new ArrayList<>();
        datasets.add(sampleDataset);
        when(datasetService.getAllDatasets()).thenReturn(datasets);

        ResponseEntity<List<Dataset>> response = datasetController.getDatasets(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(datasetService, times(1)).getAllDatasets();
    }

    @Test
    void getDatasets_WithFilter_Success() {
        List<Dataset> datasets = new ArrayList<>();
        datasets.add(sampleDataset);
        when(datasetService.getDatasetsByType("Classification")).thenReturn(datasets);

        ResponseEntity<List<Dataset>> response = datasetController.getDatasets("Classification");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(datasetService, times(1)).getDatasetsByType("Classification");
    }

    @Test
    void getDatasetById_Success() {
        when(datasetService.getDatasetById(1L)).thenReturn(Optional.of(sampleDataset));

        ResponseEntity<Dataset> response = datasetController.getDatasetById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDataset, response.getBody());
    }

    @Test
    void getDatasetById_NotFound() {
        when(datasetService.getDatasetById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Dataset> response = datasetController.getDatasetById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createDataset_Success() {
        when(datasetService.saveDataset(any(Dataset.class))).thenReturn(sampleDataset);

        ResponseEntity<Dataset> response = datasetController.createDataset(sampleDataset);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleDataset, response.getBody());
    }

    @Test
    void updateDataset_Success() {
        when(datasetService.updateDataset(eq(1L), any(Dataset.class))).thenReturn(sampleDataset);

        ResponseEntity<Dataset> response = datasetController.updateDataset(1L, sampleDataset);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleDataset, response.getBody());
    }

    @Test
    void updateDataset_NotFound() {
        when(datasetService.updateDataset(eq(99L), any(Dataset.class))).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<Dataset> response = datasetController.updateDataset(99L, sampleDataset);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteDataset_AsAdmin_Success() {
        String token = "Bearer admin_token";
        DecodedJWT mockJwt = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("ADMIN");
        doNothing().when(datasetService).deleteDataset(1L);

        ResponseEntity<?> response = datasetController.deleteDataset(1L, token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteDataset_AsUser_Forbidden() {
        String token = "Bearer user_token";
        DecodedJWT mockJwt = mock(DecodedJWT.class);
        when(jwtService.verifyToken(token)).thenReturn(mockJwt);
        when(jwtService.getRoleFromToken(mockJwt)).thenReturn("USER");

        ResponseEntity<?> response = datasetController.deleteDataset(1L, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
