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
    private final org.example.utils.JwtService jwtService;

    public ModelController(ModelService service, org.example.utils.JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
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

    // ELIMINAR MODELO (Solo ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModel(@PathVariable Long id,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        com.auth0.jwt.interfaces.DecodedJWT jwt = jwtService.verifyToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }
        String role = jwtService.getRoleFromToken(jwt);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo los administradores pueden eliminar modelos.");
        }
        try {
            service.deleteModel(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
