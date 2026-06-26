package org.example.utils;

import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "juanESCOM@ipn.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Admin ESCOM");
            admin.setEmail(adminEmail);
            // Hashear contraseña "prueba" con BCrypt
            admin.setPasswordHash(PasswordUtil.hashPassword("prueba"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("=================================================");
            System.out.println("Usuario Administrador predefinido inicializado:");
            System.out.println("Email: " + adminEmail);
            System.out.println("Rol: ADMIN");
            System.out.println("=================================================");
        } else {
            // Asegurar que si ya existe tenga el rol ADMIN
            userRepository.findByEmail(adminEmail).ifPresent(user -> {
                if (!"ADMIN".equals(user.getRole())) {
                    user.setRole("ADMIN");
                    userRepository.save(user);
                    System.out.println("Rol de " + adminEmail + " actualizado a ADMIN.");
                }
            });
        }
    }
}
