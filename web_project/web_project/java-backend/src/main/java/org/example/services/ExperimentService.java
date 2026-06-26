package org.example.services;

import org.example.entities.Experiment;
import org.example.repositories.ExperimentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ExperimentService {

    private final ExperimentRepository repository;

    public ExperimentService(
            ExperimentRepository repository
    ) {

        this.repository = repository;
    }

    // SAVE EXPERIMENT

    public Experiment saveExperiment(
            Experiment experiment
    ) {

        return repository.save(experiment);
    }

    // GET ALL EXPERIMENTS

    public List<Experiment> getAllExperiments() {

        return repository.findAll();
    }

    // GET EXPERIMENTS BY USER ID

    public List<Experiment> getExperimentsByUserId(Long userId) {

        return repository.findByUserId(userId);
    }

    // GET EXPERIMENTS BY MODEL ID

    public List<Experiment> getExperimentsByModelId(Long modelId) {

        return repository.findByModelId(modelId);
    }
}