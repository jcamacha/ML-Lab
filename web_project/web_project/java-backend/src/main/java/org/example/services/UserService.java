package org.example.services;

import org.example.entities.User;
import org.example.repositories.UserRepository;
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
            if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isBlank()) {
                user.setPasswordHash(userDetails.getPasswordHash());
            }
            return repository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // DELETE
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    // AUTHENTICATE (Comparación simple)
    public User authenticate(String email, String password) {
        return repository.findByEmail(email)
                .filter(user -> user.getPasswordHash().equals(password))
                .orElse(null);
    }
}
