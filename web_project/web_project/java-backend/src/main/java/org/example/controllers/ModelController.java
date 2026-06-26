package org.example.controllers;

import jakarta.validation.Valid;
import org.example.entities.Model;
import org.example.services.ModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/models")
@CrossOrigin(origins = "*")
public class ModelController {

    private final ModelService service;

    public ModelController(ModelService service) {
        this.service = service;
    }

    // OBTENER TODOS LOS MODELOS
    @GetMapping
    public ResponseEntity<List<Model>> getAllModels() {
        return ResponseEntity.ok(service.getAllModels());
    }

    // OBTENER MODELO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Model> getModelById(@PathVariable Long id) {
        return service.getModelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR MODELO
    @PostMapping
    public ResponseEntity<Model> createModel(@Valid @RequestBody Model model) {
        return new ResponseEntity<>(service.saveModel(model), HttpStatus.CREATED);
    }

    // ACTUALIZAR MODELO
    @PutMapping("/{id}")
    public ResponseEntity<Model> updateModel(@PathVariable Long id, @Valid @RequestBody Model modelDetails) {
        try {
            return ResponseEntity.ok(service.updateModel(id, modelDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR MODELO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        try {
            service.deleteModel(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
