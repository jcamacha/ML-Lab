package org.example.services;

import org.example.entities.UploadedFile;
import org.example.entities.User;
import org.example.repositories.UploadedFileRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UploadedFileService {

    private final UploadedFileRepository fileRepository;
    private final UserRepository userRepository;

    // Directorio local para guardar los archivos subidos
    private final String uploadDir = "uploads/";

    public UploadedFileService(UploadedFileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        
        // Crear el directorio si no existe
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // SUBIR Y GUARDAR ARCHIVO
    public UploadedFile storeFile(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + userId));

        // Generar nombre de archivo único para evitar colisiones
        String originalFileName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + (originalFileName != null ? originalFileName : "unnamed");
        Path targetLocation = Paths.get(uploadDir).toAbsolutePath().resolve(fileName);

        // Copiar el archivo al sistema de archivos
        Files.copy(file.getInputStream(), targetLocation);

        // Crear la entidad y guardar en BD
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(originalFileName);
        uploadedFile.setStoragePath(targetLocation.toString());
        uploadedFile.setFileSize(file.getSize());
        uploadedFile.setUser(user);
        uploadedFile.setUploadedAt(LocalDateTime.now());

        return fileRepository.save(uploadedFile);
    }

    // LISTAR TODOS LOS REGISTROS
    public List<UploadedFile> getAllFiles() {
        return fileRepository.findAll();
    }

    // OBTENER REGISTRO POR ID
    public Optional<UploadedFile> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    // OBTENER ARCHIVOS DE UN USUARIO
    public List<UploadedFile> getFilesByUserId(Long userId) {
        return fileRepository.findByUserUserId(userId);
    }

    // ELIMINAR ARCHIVO (FISICO Y REGISTRO)
    public void deleteFile(Long id) {
        UploadedFile uploadedFile = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado con id: " + id));

        // Borrar el archivo físico si existe
        try {
            Path filePath = Paths.get(uploadedFile.getStoragePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Loguear o reportar error en el borrado físico, pero continuar borrando de la BD
            System.err.println("No se pudo eliminar el archivo fisico: " + e.getMessage());
        }

        // Borrar de la BD
        fileRepository.deleteById(id);
    }
}
