package org.example.controllers;

import org.example.entities.UploadedFile;
import org.example.entities.User;
import org.example.services.UploadedFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadedFileControllerTest {

    @Mock
    private UploadedFileService uploadedFileService;

    @InjectMocks
    private UploadedFileController uploadedFileController;

    private UploadedFile sampleFile;
    private MultipartFile mockMultipartFile;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1L);

        sampleFile = new UploadedFile();
        sampleFile.setFileId(10L);
        sampleFile.setFileName("data.csv");
        sampleFile.setStoragePath("/uploads/data.csv");
        sampleFile.setFileSize(1024L);
        sampleFile.setUser(user);

        mockMultipartFile = mock(MultipartFile.class);
    }

    @Test
    void uploadFile_Success() throws Exception {
        when(uploadedFileService.storeFile(eq(mockMultipartFile), eq(1L))).thenReturn(sampleFile);

        ResponseEntity<UploadedFile> response = uploadedFileController.uploadFile(mockMultipartFile, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleFile, response.getBody());
    }

    @Test
    void uploadFile_Failure() throws Exception {
        when(uploadedFileService.storeFile(eq(mockMultipartFile), eq(1L))).thenThrow(new RuntimeException("Upload failed"));

        ResponseEntity<UploadedFile> response = uploadedFileController.uploadFile(mockMultipartFile, 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getFiles_NoFilter_Success() {
        List<UploadedFile> files = new ArrayList<>();
        files.add(sampleFile);
        when(uploadedFileService.getAllFiles()).thenReturn(files);

        ResponseEntity<List<UploadedFile>> response = uploadedFileController.getFiles(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(uploadedFileService, times(1)).getAllFiles();
    }

    @Test
    void getFiles_WithFilter_Success() {
        List<UploadedFile> files = new ArrayList<>();
        files.add(sampleFile);
        when(uploadedFileService.getFilesByUserId(1L)).thenReturn(files);

        ResponseEntity<List<UploadedFile>> response = uploadedFileController.getFiles(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(uploadedFileService, times(1)).getFilesByUserId(1L);
    }

    @Test
    void deleteFile_Success() {
        doNothing().when(uploadedFileService).deleteFile(10L);

        ResponseEntity<Void> response = uploadedFileController.deleteFile(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteFile_NotFound() {
        doThrow(new RuntimeException("Not Found")).when(uploadedFileService).deleteFile(99L);

        ResponseEntity<Void> response = uploadedFileController.deleteFile(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
