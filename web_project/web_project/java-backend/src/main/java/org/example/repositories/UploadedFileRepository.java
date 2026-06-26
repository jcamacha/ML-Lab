package org.example.repositories;

import org.example.entities.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    List<UploadedFile> findByUserUserId(Long userId);
}
