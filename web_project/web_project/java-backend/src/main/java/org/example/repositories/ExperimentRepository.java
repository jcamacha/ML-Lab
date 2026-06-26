package org.example.repositories;

import org.example.entities.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExperimentRepository
        extends JpaRepository<Experiment, Long> {
    List<Experiment> findByUserId(Long userId);
    List<Experiment> findByModelId(Long modelId);
}