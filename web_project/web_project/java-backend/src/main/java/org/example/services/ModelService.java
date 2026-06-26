package org.example.services;

import org.example.entities.Model;
import org.example.repositories.ModelRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ModelService {

    private final ModelRepository repository;

    public ModelService(ModelRepository repository) {
        this.repository = repository;
    }

    // CREATE / SAVE
    public Model saveModel(Model model) {
        return repository.save(model);
    }

    // GET ALL
    public List<Model> getAllModels() {
        return repository.findAll();
    }

    // GET BY ID
    public Optional<Model> getModelById(Long id) {
        return repository.findById(id);
    }

    // GET BY CATEGORY
    public List<Model> getModelsByCategory(String category) {
        return repository.findByCategory(category);
    }

    // UPDATE
    public Model updateModel(Long id, Model modelDetails) {
        return repository.findById(id).map(model -> {
            model.setName(modelDetails.getName());
            model.setCategory(modelDetails.getCategory());
            model.setDescription(modelDetails.getDescription());
            return repository.save(model);
        }).orElseThrow(() -> new RuntimeException("Modelo no encontrado con id: " + id));
    }

    // DELETE
    public void deleteModel(Long id) {
        repository.deleteById(id);
    }
}
