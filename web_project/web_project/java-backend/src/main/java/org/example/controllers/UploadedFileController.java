package org.example.controllers;

import org.example.entities.UploadedFile;
import org.example.services.UploadedFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class UploadedFileController {

    private final UploadedFileService service;

    public UploadedFileController(UploadedFileService service) {
        this.service = service;
    }

    // SUBIR ARCHIVO ASOCIADO A UN USUARIO
    @PostMapping("/upload")
    public ResponseEntity<UploadedFile> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        try {
            UploadedFile uploadedFile = service.storeFile(file, userId);
            return new ResponseEntity<>(uploadedFile, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // LISTAR ARCHIVOS (CON FILTRO OPCIONAL POR USER_ID)
    @GetMapping
    public ResponseEntity<List<UploadedFile>> getFiles(@RequestParam(value = "userId", required = false) Long userId) {
        if (userId != null) {
            return ResponseEntity.ok(service.getFilesByUserId(userId));
        }
        return ResponseEntity.ok(service.getAllFiles());
    }

    // ELIMINAR ARCHIVO REGISTRADO Y FISICO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        try {
            service.deleteFile(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
