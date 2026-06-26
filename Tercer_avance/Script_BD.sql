-- =============================================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- PROYECTO FINAL - DESARROLLO WEB
-- =============================================================================

-- Creación de la base de datos (opcional/comentada para evitar errores de permisos)
-- CREATE DATABASE IF NOT EXISTS web_project_db;
-- USE web_project_db;

-- -----------------------------------------------------------------------------
-- 1. TABLA: users (Usuarios del sistema)
-- -----------------------------------------------------------------------------
-- Almacena la información de los usuarios registrados y sus roles en la aplicación.

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 2. TABLA: datasets (Conjuntos de datos)
-- -----------------------------------------------------------------------------
-- Metadatos y detalles de los conjuntos de datos cargados para los experimentos.

CREATE TABLE IF NOT EXISTS datasets (
    dataset_id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    dataset_type VARCHAR(50),
    num_samples INT,
    num_features INT,
    target_variable VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_datasets PRIMARY KEY (dataset_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 3. TABLA: models (Catálogo de modelos de Machine Learning)
-- -----------------------------------------------------------------------------
-- Contiene la definición de los algoritmos y modelos soportados.

CREATE TABLE IF NOT EXISTS models (
    model_id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    CONSTRAINT pk_models PRIMARY KEY (model_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 4. TABLA: experiments (Resultados de experimentos)
-- -----------------------------------------------------------------------------
-- Registra los resultados de las ejecuciones, enlazando usuario, modelo y dataset.

CREATE TABLE IF NOT EXISTS experiments (
    experiment_id BIGINT AUTO_INCREMENT,
    model_name VARCHAR(255),
    dataset_name VARCHAR(255),
    accuracy DOUBLE PRECISION,
    mse DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    model_id BIGINT,
    dataset_id BIGINT,
    CONSTRAINT pk_experiments PRIMARY KEY (experiment_id),
    CONSTRAINT fk_experiments_users FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT fk_experiments_models FOREIGN KEY (model_id) REFERENCES models(model_id) ON DELETE SET NULL,
    CONSTRAINT fk_experiments_datasets FOREIGN KEY (dataset_id) REFERENCES datasets(dataset_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices para optimizar las claves foráneas en la tabla experiments
CREATE INDEX idx_experiments_user_id ON experiments(user_id);
CREATE INDEX idx_experiments_model_id ON experiments(model_id);
CREATE INDEX idx_experiments_dataset_id ON experiments(dataset_id);

-- -----------------------------------------------------------------------------
-- 5. TABLA: uploaded_files (Archivos subidos por los usuarios)
-- -----------------------------------------------------------------------------
-- Registro físico de archivos de datasets cargados en el servidor.

CREATE TABLE IF NOT EXISTS uploaded_files (
    file_id BIGINT AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    storage_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_uploaded_files PRIMARY KEY (file_id),
    CONSTRAINT fk_uploaded_files_users FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índice para optimizar la clave foránea en la tabla uploaded_files
CREATE INDEX idx_uploaded_files_user_id ON uploaded_files(user_id);


-- =============================================================================
-- INSERCIÓN DE DATOS DE EJEMPLO
-- =============================================================================

-- Datos de prueba para: users
INSERT INTO users (name, email, password_hash, role) VALUES
('Juan Pérez', 'juan.perez@example.com', '$2a$10$vI8aWBnW3f5.39E8jG0uG.wG26N7b8893N.pM7i3q28c39d8e6f1g', 'ADMIN'),
('María López', 'maria.lopez@example.com', '$2a$10$xG2f8dW3f5.39E8jG0uG.wG26N7b8893N.pM7i3q28c39d8e6f2h', 'USER'),
('Carlos Gómez', 'carlos.gomez@example.com', '$2a$10$yH3g9eW3f5.39E8jG0uG.wG26N7b8893N.pM7i3q28c39d8e6f3i', 'USER'),
('Ana Martínez', 'ana.martinez@example.com', '$2a$10$zI4haFW3f5.39E8jG0uG.wG26N7b8893N.pM7i3q28c39d8e6f4j', 'USER');

-- Datos de prueba para: datasets
INSERT INTO datasets (name, description, dataset_type, num_samples, num_features, target_variable) VALUES
('iris.csv', 'El famoso dataset de flores de Iris para tareas de clasificación de tres especies.', 'Clasificación', 150, 4, 'species'),
('boston_housing.csv', 'Datos sobre el precio de viviendas en Boston para tareas de regresión.', 'Regresión', 506, 13, 'medv'),
('titanic.csv', 'Información sobre pasajeros del Titanic útil para predicciones de supervivencia.', 'Clasificación', 891, 11, 'survived'),
('customer_churn.csv', 'Historial de clientes para predecir si abandonarán el servicio contratado.', 'Clasificación', 5000, 19, 'churn');

-- Datos de prueba para: models
INSERT INTO models (name, category, description) VALUES
('Decision Tree Classifier', 'Clasificación', 'Árbol de decisión que clasifica las muestras mediante nodos de decisión basados en entropía o Gini.'),
('Random Forest Regressor', 'Regresión', 'Ensamble de múltiples árboles de decisión entrenados en subconjuntos de datos aleatorios.'),
('Support Vector Machine (SVM)', 'Clasificación', 'Clasificador lineal o no lineal (kernel RBF) que maximiza el margen entre clases.'),
('Linear Regression', 'Regresión', 'Modelo lineal simple para aproximar la relación matemática entre variables explicativas y una continua.');

-- Datos de prueba para: experiments
INSERT INTO experiments (model_name, dataset_name, accuracy, mse, user_id, model_id, dataset_id) VALUES
('Decision Tree Classifier', 'iris.csv', 0.9667, NULL, 2, 1, 1),
('Random Forest Regressor', 'boston_housing.csv', NULL, 12.345, 2, 2, 2),
('Support Vector Machine (SVM)', 'titanic.csv', 0.8123, NULL, 3, 3, 3),
('Linear Regression', 'boston_housing.csv', NULL, 23.456, 4, 4, 2);

-- Datos de prueba para: uploaded_files
INSERT INTO uploaded_files (file_name, storage_path, file_size, user_id) VALUES
('iris_modified.csv', '/uploads/datasets/12345_iris_modified.csv', 4500, 2),
('housing_v2.csv', '/uploads/datasets/23456_housing_v2.csv', 32000, 2),
('titanic_test.csv', '/uploads/datasets/34567_titanic_test.csv', 18000, 3),
('churn_telecom.csv', '/uploads/datasets/45678_churn_telecom.csv', 145000, 4);
