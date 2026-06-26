package org.example.repositories;

import org.example.entities.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findByDatasetType(String datasetType);
}
