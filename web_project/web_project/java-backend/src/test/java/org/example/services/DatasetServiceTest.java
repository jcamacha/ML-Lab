package org.example.services;

import org.example.entities.Dataset;
import org.example.repositories.DatasetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatasetServiceTest {

    @Mock
    private DatasetRepository datasetRepository;

    @InjectMocks
    private DatasetService datasetService;

    private Dataset sampleDataset;

    @BeforeEach
    void setUp() {
        sampleDataset = new Dataset();
        sampleDataset.setDatasetId(1L);
        sampleDataset.setName("Iris");
        sampleDataset.setDescription("Iris dataset");
        sampleDataset.setDatasetType("Classification");
        sampleDataset.setNumSamples(150);
        sampleDataset.setNumFeatures(4);
        sampleDataset.setTargetVariable("class");
    }

    @Test
    void saveDataset_Success() {
        when(datasetRepository.save(any(Dataset.class))).thenReturn(sampleDataset);

        Dataset result = datasetService.saveDataset(sampleDataset);

        assertNotNull(result);
        assertEquals("Iris", result.getName());
    }

    @Test
    void getAllDatasets_Success() {
        List<Dataset> list = new ArrayList<>();
        list.add(sampleDataset);
        when(datasetRepository.findAll()).thenReturn(list);

        List<Dataset> result = datasetService.getAllDatasets();

        assertEquals(1, result.size());
        assertEquals(sampleDataset, result.get(0));
    }

    @Test
    void getDatasetById_Success() {
        when(datasetRepository.findById(1L)).thenReturn(Optional.of(sampleDataset));

        Optional<Dataset> result = datasetService.getDatasetById(1L);

        assertTrue(result.isPresent());
        assertEquals(sampleDataset, result.get());
    }

    @Test
    void getDatasetsByType_Success() {
        List<Dataset> list = new ArrayList<>();
        list.add(sampleDataset);
        when(datasetRepository.findByDatasetType("Classification")).thenReturn(list);

        List<Dataset> result = datasetService.getDatasetsByType("Classification");

        assertEquals(1, result.size());
        assertEquals("Classification", result.get(0).getDatasetType());
    }

    @Test
    void updateDataset_Success() {
        Dataset newDetails = new Dataset();
        newDetails.setName("New Iris");
        newDetails.setDescription("New description");
        newDetails.setDatasetType("Reg");
        newDetails.setNumSamples(200);
        newDetails.setNumFeatures(5);
        newDetails.setTargetVariable("target");

        when(datasetRepository.findById(1L)).thenReturn(Optional.of(sampleDataset));
        when(datasetRepository.save(any(Dataset.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Dataset result = datasetService.updateDataset(1L, newDetails);

        assertEquals("New Iris", result.getName());
        assertEquals("New description", result.getDescription());
        assertEquals("Reg", result.getDatasetType());
        assertEquals(200, result.getNumSamples());
        assertEquals(5, result.getNumFeatures());
        assertEquals("target", result.getTargetVariable());
    }

    @Test
    void updateDataset_NotFound_ThrowsException() {
        when(datasetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> datasetService.updateDataset(99L, sampleDataset));
    }

    @Test
    void deleteDataset_Success() {
        doNothing().when(datasetRepository).deleteById(1L);

        datasetService.deleteDataset(1L);

        verify(datasetRepository, times(1)).deleteById(1L);
    }
}
