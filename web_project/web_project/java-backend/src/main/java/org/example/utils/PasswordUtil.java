package org.example.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hashear una contraseña usando BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    // Verificar si una contraseña coincide con su hash
    public static boolean verifyPassword(String password, String hashed) {
        if (hashed == null || !hashed.startsWith("$2a$")) {
            // Manejo de contingencia por si hay contraseñas en texto plano heredadas
            return password.equals(hashed);
        }
        try {
            return BCrypt.checkpw(password, hashed);
        } catch (Exception e) {
            return false;
        }
    }
}
