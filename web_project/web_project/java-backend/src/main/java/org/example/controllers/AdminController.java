package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.entities.User;
import org.example.services.UserService;
import org.example.services.DatasetService;
import org.example.services.ModelService;
import org.example.services.ExperimentService;
import org.example.services.UploadedFileService;
import org.example.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserService userService;
    private final DatasetService datasetService;
    private final ModelService modelService;
    private final ExperimentService experimentService;
    private final UploadedFileService uploadedFileService;
    private final JwtService jwtService;

    public AdminController(UserService userService,
                           DatasetService datasetService,
                           ModelService modelService,
                           ExperimentService experimentService,
                           UploadedFileService uploadedFileService,
                           JwtService jwtService) {
        this.userService = userService;
        this.datasetService = datasetService;
        this.modelService = modelService;
        this.experimentService = experimentService;
        this.uploadedFileService = uploadedFileService;
        this.jwtService = jwtService;
    }

    private boolean isAdmin(String authHeader) {
        DecodedJWT jwt = jwtService.verifyToken(authHeader);
        if (jwt != null) {
            String role = jwtService.getRoleFromToken(jwt);
            return "ADMIN".equals(role);
        }
        return false;
    }

    // OBTENER TODOS LOS USUARIOS (Solo ADMIN)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ELIMINAR UN USUARIO (Solo ADMIN)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR UN DATASET (Solo ADMIN)
    @DeleteMapping("/datasets/{id}")
    public ResponseEntity<?> deleteDataset(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        try {
            datasetService.deleteDataset(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR UN MODELO (Solo ADMIN)
    @DeleteMapping("/models/{id}")
    public ResponseEntity<?> deleteModel(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        try {
            modelService.deleteModel(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ESTADISTICAS DEL SISTEMA (Solo ADMIN)
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isAdmin(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getAllUsers().size());
        stats.put("totalDatasets", datasetService.getAllDatasets().size());
        stats.put("totalModels", modelService.getAllModels().size());
        stats.put("totalExperiments", experimentService.getAllExperiments().size());
        stats.put("totalFiles", uploadedFileService.getAllFiles().size());
        
        return ResponseEntity.ok(stats);
    }
}
