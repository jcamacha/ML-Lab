# Diccionario de Datos

Este documento contiene la estructura y descripción de las tablas que conforman la base de datos del sistema, derivado de las entidades en la capa de persistencia del backend.

---

## Tabla: `users`
**Descripción:** Almacena la información de registro de los usuarios del sistema, sus credenciales hash y sus roles.

| Nombre de columna | Tipo | Tamaño | Nullable | Default | Descripción | PK/FK |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `user_id` | BIGINT | - | NO | *AUTO_INCREMENT* | Identificador único autoincremental del usuario. | PK |
| `name` | VARCHAR | 100 | NO | Ninguno | Nombre completo del usuario. | - |
| `email` | VARCHAR | 120 | NO | Ninguno | Correo electrónico único del usuario. | UNIQUE |
| `password_hash` | VARCHAR | 255 | NO | Ninguno | Hash de seguridad de la contraseña del usuario. | - |
| `role` | VARCHAR | 20 | SÍ | `'USER'` | Rol del usuario para control de acceso (ej. USER, ADMIN). | - |
| `created_at` | TIMESTAMP | - | SÍ | *CURRENT_TIMESTAMP* | Fecha y hora en la que se registró el usuario. | - |

---

## Tabla: `datasets`
**Descripción:** Registra la información y metadatos de los conjuntos de datos (datasets) cargados o disponibles en la plataforma.

| Nombre de columna | Tipo | Tamaño | Nullable | Default | Descripción | PK/FK |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `dataset_id` | BIGINT | - | NO | *AUTO_INCREMENT* | Identificador único del dataset. | PK |
| `name` | VARCHAR | 100 | NO | Ninguno | Nombre identificador del dataset. | - |
| `description` | TEXT | - | SÍ | Ninguno | Descripción o detalles del contenido del dataset. | - |
| `dataset_type` | VARCHAR | 50 | SÍ | Ninguno | Tipo de dataset (ej. Clasificación, Regresión). | - |
| `num_samples` | INT | - | SÍ | Ninguno | Número de muestras o registros que contiene el dataset. | - |
| `num_features` | INT | - | SÍ | Ninguno | Cantidad de atributos o características del dataset. | - |
| `target_variable` | VARCHAR | 100 | SÍ | Ninguno | Nombre de la variable objetivo o etiqueta a predecir. | - |
| `created_at` | TIMESTAMP | - | SÍ | *CURRENT_TIMESTAMP* | Fecha y hora de registro del dataset. | - |

---

## Tabla: `models`
**Descripción:** Catálogo de algoritmos y modelos de Machine Learning preestablecidos en el sistema para realizar experimentos.

| Nombre de columna | Tipo | Tamaño | Nullable | Default | Descripción | PK/FK |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `model_id` | BIGINT | - | NO | *AUTO_INCREMENT* | Identificador único del modelo. | PK |
| `name` | VARCHAR | 100 | NO | Ninguno | Nombre común del modelo (ej. Random Forest, SVM). | - |
| `category` | VARCHAR | 50 | NO | Ninguno | Categoría o tipo de tarea (ej. Clasificación, Regresión). | - |
| `description` | TEXT | - | SÍ | Ninguno | Descripción del modelo y su funcionamiento teórico. | - |

---

## Tabla: `experiments`
**Descripción:** Registra las ejecuciones y los resultados (métricas obtenidas) de entrenar modelos de Machine Learning con datasets.

| Nombre de columna | Tipo | Tamaño | Nullable | Default | Descripción | PK/FK |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `experiment_id` | BIGINT | - | NO | *AUTO_INCREMENT* | Identificador único autoincremental del experimento. | PK |
| `model_name` | VARCHAR | 255 | SÍ | Ninguno | Copia del nombre del modelo usado. | - |
| `dataset_name` | VARCHAR | 255 | SÍ | Ninguno | Copia del nombre del dataset usado. | - |
| `accuracy` | DOUBLE | - | SÍ | Ninguno | Exactitud (Accuracy) obtenida para tareas de clasificación. | - |
| `mse` | DOUBLE | - | SÍ | Ninguno | Error Cuadrático Medio (MSE) obtenido para tareas de regresión. | - |
| `created_at` | TIMESTAMP | - | SÍ | *CURRENT_TIMESTAMP* | Fecha y hora de creación/ejecución del experimento. | - |
| `user_id` | BIGINT | - | SÍ | Ninguno | Referencia al usuario creador del experimento. | FK (users.user_id) |
| `model_id` | BIGINT | - | SÍ | Ninguno | Referencia al modelo de Machine Learning utilizado. | FK (models.model_id) |
| `dataset_id` | BIGINT | - | SÍ | Ninguno | Referencia al dataset de Machine Learning utilizado. | FK (datasets.dataset_id) |

---

## Tabla: `uploaded_files`
**Descripción:** Almacena los archivos subidos al sistema por los usuarios, incluyendo su tamaño y ruta en el almacenamiento.

| Nombre de columna | Tipo | Tamaño | Nullable | Default | Descripción | PK/FK |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `file_id` | BIGINT | - | NO | *AUTO_INCREMENT* | Identificador único del archivo subido. | PK |
| `file_name` | VARCHAR | 255 | NO | Ninguno | Nombre original del archivo subido por el usuario. | - |
| `storage_path` | VARCHAR | 255 | NO | Ninguno | Ruta física del archivo guardado en el servidor. | - |
| `file_size` | BIGINT | - | SÍ | Ninguno | Tamaño del archivo en bytes. | - |
| `uploaded_at` | TIMESTAMP | - | SÍ | *CURRENT_TIMESTAMP* | Fecha y hora en la que se subió el archivo. | - |
| `user_id` | BIGINT | - | NO | Ninguno | Referencia del usuario propietario del archivo. | FK (users.user_id) |
