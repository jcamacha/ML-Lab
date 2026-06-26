package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "experiments")

public class Experiment{

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

    public Long getExperiment_id() {
        return experiment_id;
    }

    public void setExperiment_id(Long experiment_id) {
        this.experiment_id = experiment_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getDataset_name() {
        return dataset_name;
    }

    public void setDataset_name(String dataset_name) {
        this.dataset_name = dataset_name;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Double getMse() {
        return mse;
    }

    public void setMse(Double mse) {
        this.mse = mse;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
}