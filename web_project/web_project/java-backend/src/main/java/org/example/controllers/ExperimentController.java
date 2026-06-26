package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.Experiment;
import org.example.services.ExperimentService;
import org.example.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/experiments")
@CrossOrigin(origins = "*")
public class ExperimentController {

    private final ExperimentService service;
    private final JwtService jwtService;

    public ExperimentController(ExperimentService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    private DecodedJWT validateToken(String authHeader) {
        return jwtService.verifyToken(authHeader);
    }

    // SAVE EXPERIMENT
    @PostMapping
    public ResponseEntity<?> saveExperiment(@RequestBody Experiment experiment,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = validateToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado. Token inválido o ausente.");
        }
        
        Long userId = jwtService.getUserIdFromToken(jwt);
        String role = jwtService.getRoleFromToken(jwt);
        
        // El usuario normal solo puede guardar experimentos bajo su propia cuenta
        if (!"ADMIN".equals(role)) {
            experiment.setUserId(userId);
        } else if (experiment.getUserId() == null) {
            experiment.setUserId(userId);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveExperiment(experiment));
    }

    // GET ALL EXPERIMENTS (ADMIN ve todos, USER ve solo los suyos)
    @GetMapping
    public ResponseEntity<?> getAllExperiments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = validateToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado. Token inválido o ausente.");
        }

        Long userId = jwtService.getUserIdFromToken(jwt);
        String role = jwtService.getRoleFromToken(jwt);

        if ("ADMIN".equals(role)) {
            return ResponseEntity.ok(service.getAllExperiments());
        } else {
            return ResponseEntity.ok(service.getExperimentsByUserId(userId));
        }
    }

    // GET EXPERIMENTS BY USER ID (Solo el usuario o el ADMIN pueden consultarlo)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getExperimentsByUserId(@PathVariable Long userId,
                                                    @RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = validateToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }

        Long tokenUserId = jwtService.getUserIdFromToken(jwt);
        String role = jwtService.getRoleFromToken(jwt);

        if (!"ADMIN".equals(role) && !tokenUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. No puedes ver experimentos de otros usuarios.");
        }

        return ResponseEntity.ok(service.getExperimentsByUserId(userId));
    }

    // GET EXPERIMENTS BY MODEL ID (Solo para usuarios autenticados)
    @GetMapping("/model/{modelId}")
    public ResponseEntity<?> getExperimentsByModelId(@PathVariable Long modelId,
                                                     @RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = validateToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }
        return ResponseEntity.ok(service.getExperimentsByModelId(modelId));
    }

    // DELETE EXPERIMENT (Solo el creador del experimento o un ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExperiment(@PathVariable Long id,
                                              @RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = validateToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }

        Long tokenUserId = jwtService.getUserIdFromToken(jwt);
        String role = jwtService.getRoleFromToken(jwt);

        return service.getExperimentById(id).map(experiment -> {
            if (!"ADMIN".equals(role) && !tokenUserId.equals(experiment.getUserId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Acceso denegado. No puedes eliminar experimentos de otros usuarios.");
            }
            service.deleteExperiment(id);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}