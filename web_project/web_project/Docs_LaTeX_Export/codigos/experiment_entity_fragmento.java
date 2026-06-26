// FRAGMENTO de: java-backend/src/main/java/org/example/entities/Experiment.java (líneas 1-39)
// Entidad JPA que representa la ejecución y resultados de un experimento de Machine Learning.

package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "experiments")
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_id", insertable = false)
    private Long experiment_id;

    private String model_name;
    private String dataset_name;
    private Double accuracy;
    private Double mse;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime created_at;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "model_id")
    private Long modelId;

    public Experiment() {
        this.created_at = LocalDateTime.now();
    }
    
    // Getters y Setters omitidos por brevedad...
}
