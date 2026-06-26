package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "datasets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dataset_id")
    private Long datasetId;

    @NotBlank(message = "El nombre del dataset es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 50, message = "El tipo de dataset no puede superar los 50 caracteres")
    @Column(name = "dataset_type", length = 50)
    private String datasetType;

    @Column(name = "num_samples")
    private Integer numSamples;

    @Column(name = "num_features")
    private Integer numFeatures;

    @Size(max = 100, message = "La variable objetivo no puede superar los 100 caracteres")
    @Column(name = "target_variable", length = 100)
    private String targetVariable;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
