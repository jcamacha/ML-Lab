package org.example.controllers;

import jakarta.validation.Valid;
import org.example.entities.Dataset;
import org.example.services.DatasetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@CrossOrigin(origins = "*")
public class DatasetController {

    private final DatasetService service;

    public DatasetController(DatasetService service) {
        this.service = service;
    }

    // OBTENER TODOS LOS DATASETS (CON FILTRO OPCIONAL POR TIPO)
    @GetMapping
    public ResponseEntity<List<Dataset>> getDatasets(@RequestParam(value = "type", required = false) String type) {
        if (type != null && !type.isBlank()) {
            return ResponseEntity.ok(service.getDatasetsByType(type));
        }
        return ResponseEntity.ok(service.getAllDatasets());
    }

    // OBTENER DATASET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Dataset> getDatasetById(@PathVariable Long id) {
        return service.getDatasetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR DATASET
    @PostMapping
    public ResponseEntity<Dataset> createDataset(@Valid @RequestBody Dataset dataset) {
        return new ResponseEntity<>(service.saveDataset(dataset), HttpStatus.CREATED);
    }

    // ACTUALIZAR DATASET
    @PutMapping("/{id}")
    public ResponseEntity<Dataset> updateDataset(@PathVariable Long id, @Valid @RequestBody Dataset datasetDetails) {
        try {
            return ResponseEntity.ok(service.updateDataset(id, datasetDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR DATASET
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataset(@PathVariable Long id) {
        try {
            service.deleteDataset(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
