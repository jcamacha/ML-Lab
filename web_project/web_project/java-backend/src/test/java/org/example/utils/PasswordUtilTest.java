package org.example.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void hashPassword_GeneratesValidBCryptHash() {
        String password = "mySecretPassword123";
        String hashed = PasswordUtil.hashPassword(password);

        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$2a$"));
    }

    @Test
    void verifyPassword_MatchingPassword_ReturnsTrue() {
        String password = "mySecretPassword123";
        String hashed = PasswordUtil.hashPassword(password);

        assertTrue(PasswordUtil.verifyPassword(password, hashed));
    }

    @Test
    void verifyPassword_NonMatchingPassword_ReturnsFalse() {
        String password = "mySecretPassword123";
        String hashed = PasswordUtil.hashPassword(password);

        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hashed));
    }

    @Test
    void verifyPassword_PlainTextFallback_ReturnsTrue() {
        String plainText = "plainTextPassword";
        // If hashed does not start with $2a$, it does a direct string comparison
        assertTrue(PasswordUtil.verifyPassword(plainText, plainText));
    }

    @Test
    void verifyPassword_PlainTextFallback_ReturnsFalse() {
        assertFalse(PasswordUtil.verifyPassword("pass1", "pass2"));
    }

    @Test
    void verifyPassword_NullHash_ReturnsFalse() {
        assertFalse(PasswordUtil.verifyPassword("password", null));
    }

    @Test
    void verifyPassword_InvalidHashFormat_ReturnsFalse() {
        assertFalse(PasswordUtil.verifyPassword("password", "$2a$10$invalidhashpatternlongenoughtofail"));
    }
}
