package org.example.services;

import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.example.utils.PasswordUtil;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // CREATE / SAVE
    public User saveUser(User user) {
        if (user.getPasswordHash() != null && !user.getPasswordHash().startsWith("$2a$")) {
            user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        return repository.save(user);
    }

    // REGISTRO DE USUARIOS
    public User register(User user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        user.setRole("USER");
        user.setPasswordHash(PasswordUtil.hashPassword(user.getPasswordHash()));
        return repository.save(user);
    }

    // GET ALL
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // GET BY ID
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    // UPDATE
    public User updateUser(Long id, User userDetails) {
        return repository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            if (userDetails.getRole() != null && !userDetails.getRole().isBlank()) {
                user.setRole(userDetails.getRole());
            }
            if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isBlank()) {
                // Hashear la contraseña sólo si se ha cambiado (no empieza con hash BCrypt)
                if (!userDetails.getPasswordHash().startsWith("$2a$")) {
                    user.setPasswordHash(PasswordUtil.hashPassword(userDetails.getPasswordHash()));
                } else {
                    user.setPasswordHash(userDetails.getPasswordHash());
                }
            }
            return repository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // DELETE
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    // AUTHENTICATE (Comparación usando BCrypt)
    public User authenticate(String email, String password) {
        return repository.findByEmail(email)
                .filter(user -> PasswordUtil.verifyPassword(password, user.getPasswordHash()))
                .orElse(null);
    }

    // LOGIN (Alias of authenticate or direct implementation using PasswordUtil)
    public User login(String email, String password) {
        return authenticate(email, password);
    }

    // FIND BY EMAIL
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
