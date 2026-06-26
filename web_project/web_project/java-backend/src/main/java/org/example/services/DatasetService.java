package org.example.services;

import org.example.entities.Dataset;
import org.example.repositories.DatasetRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DatasetService {

    private final DatasetRepository repository;

    public DatasetService(DatasetRepository repository) {
        this.repository = repository;
    }

    // CREATE / SAVE
    public Dataset saveDataset(Dataset dataset) {
        return repository.save(dataset);
    }

    // GET ALL
    public List<Dataset> getAllDatasets() {
        return repository.findAll();
    }

    // GET BY ID
    public Optional<Dataset> getDatasetById(Long id) {
        return repository.findById(id);
    }

    // GET BY TYPE
    public List<Dataset> getDatasetsByType(String type) {
        return repository.findByDatasetType(type);
    }

    // UPDATE
    public Dataset updateDataset(Long id, Dataset datasetDetails) {
        return repository.findById(id).map(dataset -> {
            dataset.setName(datasetDetails.getName());
            dataset.setDescription(datasetDetails.getDescription());
            dataset.setDatasetType(datasetDetails.getDatasetType());
            dataset.setNumSamples(datasetDetails.getNumSamples());
            dataset.setNumFeatures(datasetDetails.getNumFeatures());
            dataset.setTargetVariable(datasetDetails.getTargetVariable());
            return repository.save(dataset);
        }).orElseThrow(() -> new RuntimeException("Dataset no encontrado con id: " + id));
    }

    // DELETE
    public void deleteDataset(Long id) {
        repository.deleteById(id);
    }
}
