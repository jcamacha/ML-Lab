package org.example.controllers;

import org.example.entities.Experiment;
import org.example.services.ExperimentService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/experiments")

@CrossOrigin(origins = "*")

public class ExperimentController {

    private final ExperimentService service;

    public ExperimentController(
            ExperimentService service
    ) {

        this.service = service;
    }

    // SAVE EXPERIMENT

    @PostMapping

    public Experiment saveExperiment(
            @RequestBody Experiment experiment
    ) {

        return service.saveExperiment(experiment);
    }

    // GET ALL EXPERIMENTS

    @GetMapping

    public List<Experiment> getAllExperiments() {

        return service.getAllExperiments();
    }

    // GET EXPERIMENTS BY USER ID

    @GetMapping("/user/{userId}")

    public List<Experiment> getExperimentsByUserId(@PathVariable Long userId) {

        return service.getExperimentsByUserId(userId);
    }

    // GET EXPERIMENTS BY MODEL ID

    @GetMapping("/model/{modelId}")

    public List<Experiment> getExperimentsByModelId(@PathVariable Long modelId) {

        return service.getExperimentsByModelId(modelId);
    }
}