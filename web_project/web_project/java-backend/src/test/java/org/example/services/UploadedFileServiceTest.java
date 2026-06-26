package org.example.services;

import org.example.entities.UploadedFile;
import org.example.entities.User;
import org.example.repositories.UploadedFileRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadedFileServiceTest {

    @Mock
    private UploadedFileRepository fileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UploadedFileService uploadedFileService;

    private User sampleUser;
    private UploadedFile sampleFile;
    private List<Path> tempFilesCreated;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);

        sampleFile = new UploadedFile();
        sampleFile.setFileId(10L);
        sampleFile.setFileName("test.csv");
        sampleFile.setStoragePath("uploads/test_temp.csv");
        sampleFile.setUser(sampleUser);

        tempFilesCreated = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        for (Path path : tempFilesCreated) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ignored) {}
        }
    }

    @Test
    void storeFile_Success() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.csv");
        when(multipartFile.getSize()).thenReturn(100L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(fileRepository.save(any(UploadedFile.class))).thenAnswer(invocation -> {
            UploadedFile f = invocation.getArgument(0);
            tempFilesCreated.add(Paths.get(f.getStoragePath()));
            return f;
        });

        UploadedFile result = uploadedFileService.storeFile(multipartFile, 1L);

        assertNotNull(result);
        assertEquals("test.csv", result.getFileName());
        assertEquals(100L, result.getFileSize());
        assertEquals(sampleUser, result.getUser());
    }

    @Test
    void storeFile_UserNotFound_ThrowsException() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> uploadedFileService.storeFile(multipartFile, 99L));
    }

    @Test
    void getAllFiles_Success() {
        List<UploadedFile> files = new ArrayList<>();
        files.add(sampleFile);
        when(fileRepository.findAll()).thenReturn(files);

        List<UploadedFile> result = uploadedFileService.getAllFiles();

        assertEquals(1, result.size());
        assertEquals(sampleFile, result.get(0));
    }

    @Test
    void getFileById_Success() {
        when(fileRepository.findById(10L)).thenReturn(Optional.of(sampleFile));

        Optional<UploadedFile> result = uploadedFileService.getFileById(10L);

        assertTrue(result.isPresent());
        assertEquals(sampleFile, result.get());
    }

    @Test
    void getFilesByUserId_Success() {
        List<UploadedFile> files = new ArrayList<>();
        files.add(sampleFile);
        when(fileRepository.findByUserUserId(1L)).thenReturn(files);

        List<UploadedFile> result = uploadedFileService.getFilesByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(sampleFile, result.get(0));
    }

    @Test
    void deleteFile_Success() throws IOException {
        // Create actual temp file to test deleteIfExists
        Path path = Paths.get("uploads/test_to_delete.csv");
        Files.write(path, "data".getBytes());

        sampleFile.setStoragePath(path.toAbsolutePath().toString());

        when(fileRepository.findById(10L)).thenReturn(Optional.of(sampleFile));
        doNothing().when(fileRepository).deleteById(10L);

        uploadedFileService.deleteFile(10L);

        assertFalse(Files.exists(path));
        verify(fileRepository, times(1)).deleteById(10L);
    }
}
