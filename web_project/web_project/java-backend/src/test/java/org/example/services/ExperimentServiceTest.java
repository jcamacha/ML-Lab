package org.example.services;

import org.example.entities.Experiment;
import org.example.repositories.ExperimentRepository;
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
public class ExperimentServiceTest {

    @Mock
    private ExperimentRepository experimentRepository;

    @InjectMocks
    private ExperimentService experimentService;

    private Experiment sampleExperiment;

    @BeforeEach
    void setUp() {
        sampleExperiment = new Experiment();
        sampleExperiment.setExperiment_id(1L);
        sampleExperiment.setModel_name("Linear Regression");
        sampleExperiment.setDataset_name("Iris");
        sampleExperiment.setAccuracy(0.95);
        sampleExperiment.setMse(0.02);
        sampleExperiment.setUserId(2L);
        sampleExperiment.setModelId(3L);
    }

    @Test
    void saveExperiment_Success() {
        when(experimentRepository.save(any(Experiment.class))).thenReturn(sampleExperiment);

        Experiment result = experimentService.saveExperiment(sampleExperiment);

        assertNotNull(result);
        assertEquals("Linear Regression", result.getModel_name());
        verify(experimentRepository, times(1)).save(sampleExperiment);
    }

    @Test
    void getAllExperiments_Success() {
        List<Experiment> list = new ArrayList<>();
        list.add(sampleExperiment);
        when(experimentRepository.findAll()).thenReturn(list);

        List<Experiment> result = experimentService.getAllExperiments();

        assertEquals(1, result.size());
        assertEquals(sampleExperiment, result.get(0));
    }

    @Test
    void getExperimentsByUserId_Success() {
        List<Experiment> list = new ArrayList<>();
        list.add(sampleExperiment);
        when(experimentRepository.findByUserId(2L)).thenReturn(list);

        List<Experiment> result = experimentService.getExperimentsByUserId(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getUserId());
    }

    @Test
    void getExperimentsByModelId_Success() {
        List<Experiment> list = new ArrayList<>();
        list.add(sampleExperiment);
        when(experimentRepository.findByModelId(3L)).thenReturn(list);

        List<Experiment> result = experimentService.getExperimentsByModelId(3L);

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getModelId());
    }

    @Test
    void getExperimentById_Success() {
        when(experimentRepository.findById(1L)).thenReturn(Optional.of(sampleExperiment));

        Optional<Experiment> result = experimentService.getExperimentById(1L);

        assertTrue(result.isPresent());
        assertEquals(sampleExperiment, result.get());
    }

    @Test
    void deleteExperiment_Success() {
        doNothing().when(experimentRepository).deleteById(1L);

        experimentService.deleteExperiment(1L);

        verify(experimentRepository, times(1)).deleteById(1L);
    }
}
