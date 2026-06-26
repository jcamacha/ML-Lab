package org.example.repositories;

import org.example.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findByCategory(String category);
}
