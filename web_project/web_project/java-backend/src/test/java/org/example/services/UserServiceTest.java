package org.example.services;

import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.example.utils.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setName("User One");
        sampleUser.setEmail("user@example.com");
        sampleUser.setPasswordHash("rawpassword");
        sampleUser.setRole("USER");
    }

    @Test
    void saveUser_WithPlainPassword_HashesIt() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.saveUser(sampleUser);

        assertNotNull(result.getPasswordHash());
        assertTrue(result.getPasswordHash().startsWith("$2a$"));
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void register_Success() {
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register(sampleUser);

        assertEquals("USER", result.getRole());
        assertTrue(result.getPasswordHash().startsWith("$2a$"));
    }

    @Test
    void register_ExistingEmail_ThrowsException() {
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.register(sampleUser));
        assertEquals("El correo ya está registrado", exception.getMessage());
    }

    @Test
    void getAllUsers_Success() {
        List<User> list = new ArrayList<>();
        list.add(sampleUser);
        when(userRepository.findAll()).thenReturn(list);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(sampleUser, result.get(0));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(sampleUser, result.get());
    }

    @Test
    void updateUser_Success() {
        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setEmail("new@example.com");
        updatedDetails.setPasswordHash("newPassword");
        updatedDetails.setRole("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updatedDetails);

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
        assertTrue(result.getPasswordHash().startsWith("$2a$"));
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void authenticate_Success() {
        // First hash password to ensure verifyPassword matches
        String hashed = PasswordUtil.hashPassword("secret");
        sampleUser.setPasswordHash(hashed);

        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        User result = userService.authenticate(sampleUser.getEmail(), "secret");

        assertNotNull(result);
        assertEquals(sampleUser, result);
    }

    @Test
    void authenticate_Failure() {
        sampleUser.setPasswordHash(PasswordUtil.hashPassword("secret"));
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

        User result = userService.authenticate(sampleUser.getEmail(), "wrongsecret");

        assertNull(result);
    }
}
