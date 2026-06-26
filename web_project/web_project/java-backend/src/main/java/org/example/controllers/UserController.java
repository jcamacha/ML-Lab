package org.example.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.example.entities.User;
import org.example.services.UserService;
import org.example.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;
    private final JwtService jwtService;

    public UserController(UserService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    private boolean isAdmin(DecodedJWT jwt) {
        return jwt != null && "ADMIN".equals(jwtService.getRoleFromToken(jwt));
    }

    private boolean isSelfOrAdmin(Long id, String authHeader) {
        DecodedJWT jwt = jwtService.verifyToken(authHeader);
        if (jwt == null) return false;
        Long tokenUserId = jwtService.getUserIdFromToken(jwt);
        return isAdmin(jwt) || (tokenUserId != null && tokenUserId.equals(id));
    }

    // OBTENER TODOS LOS USUARIOS (Solo ADMIN)
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = jwtService.verifyToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }
        if (!isAdmin(jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Se requieren permisos de administrador.");
        }
        return ResponseEntity.ok(service.getAllUsers());
    }

    // OBTENER USUARIO POR ID (Propio usuario o ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isSelfOrAdmin(id, authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. No puedes ver datos de otros usuarios.");
        }
        return service.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // REGISTRAR / CREAR USUARIO (Cualquiera puede registrarse, pero si es a través de este endpoint le asignamos USER por defecto)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(service.saveUser(user), HttpStatus.CREATED);
    }

    // ACTUALIZAR USUARIO (Propio usuario o ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails,
                                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!isSelfOrAdmin(id, authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. No puedes modificar datos de otros usuarios.");
        }
        try {
            // El usuario regular no puede promoverse a ADMIN a sí mismo
            DecodedJWT jwt = jwtService.verifyToken(authHeader);
            if (!isAdmin(jwt)) {
                userDetails.setRole("USER"); 
            }
            return ResponseEntity.ok(service.updateUser(id, userDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ELIMINAR USUARIO (Solo ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @RequestHeader(value = "Authorization", required = false) String authHeader) {
        DecodedJWT jwt = jwtService.verifyToken(authHeader);
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado.");
        }
        if (!isAdmin(jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. Solo administradores pueden eliminar usuarios.");
        }
        try {
            service.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // LOGIN DE USUARIO (Obsoleto, redirige internamente a la lógica de AuthController devolviendo un token simulado si se requiere retrocompatibilidad)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = service.authenticate(loginRequest.email(), loginRequest.password());
        if (user != null) {
            // Retorna el token JWT en la cabecera para soporte
            String token = jwtService.generateToken(user);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("token", token);
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales invalidas. Correo o contrasena incorrectos.");
        }
    }

    // DTO Record para el login
    public record LoginRequest(String email, String password) {}
}
