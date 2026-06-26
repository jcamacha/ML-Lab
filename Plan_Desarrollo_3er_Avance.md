# Plan de Desarrollo — Tercer Avance: ML Lab Full-Stack
> **Proyecto:** ML Lab — Laboratorio Interactivo de Machine Learning  
> **Stack:** Angular 18+ (Frontend) + Spring Boot 3.3.2 (Backend) + PostgreSQL Supabase  
> **Equipo:** 4 integrantes  
> **Fecha inicio:** Junio 2026  
> **Metodología:** Software Factory Multi-Agente v2.0 (`.agents/agents.md`)

---

## 📋 Requisitos del Tercer Avance (checklist del profesor)

- [x] **Stack definido:** Backend Spring Boot → Frontend **Angular** (regla: si back es Spring, front debe ser Angular)
- [ ] Desarrollar Frontend en Angular con funcionalidad completa
- [ ] Integrar Backend ↔ Frontend
- [ ] **5 entidades mínimo** (users, datasets, models, experiments, uploaded_files)
- [ ] Diccionario de Datos
- [ ] Modelo E-R de la Base de Datos
- [ ] Script de Creación de la Base de Datos
- [ ] Envío de correos electrónicos (ya parcial en 2° avance)
- [ ] Generación de documentos PDF
- [ ] URL del Frontend desplegado (Netlify, Vercel, GitHub Pages, etc.)
- [ ] URL del Backend desplegado (Render, Railway, etc.)
- [ ] Código Fuente Completo (Backend + Frontend)
- [ ] Liga del repositorio GitHub/GitLab

---

## 🗂️ Estructura de Directorios Esperada al Finalizar

```
proyecto-final-web/
├── .agents/                        # Multi-agente skills (NO incluir en git)
├── .gitignore                      # ← Debe incluir .agents/
├── README.md                       # ← Documentación general del proyecto
├── DEV_JOURNAL_ADR.md              # ← Bitácora del Orquestador
├── Segundo_avance.pdf              # ← Documento del 2° avance
├── Tercer_avance/
│   ├── Diccionario_de_Datos.md
│   ├── Modelo_ER.png
│   ├── Script_BD.sql
│   └── Plan_Desarrollo_3er_Avance.md
├── web_project/
│   ├── java-backend/               # Spring Boot (existente — COMPLETAR)
│   │   ├── src/main/java/org/example/
│   │   │   ├── entities/           # ← Completar 5 entidades JPA
│   │   │   ├── repositories/       # ← Completar repositorios
│   │   │   ├── services/           # ← Completar servicios + PDF
│   │   │   └── controllers/        # ← Completar controladores REST
│   │   ├── src/main/resources/
│   │   │   ├── application.properties
│   │   │   └── templates/          # ← Plantillas PDF
│   │   ├── pom.xml
│   │   └── Dockerfile
│   ├── python-api/                 # Microservicio Python ML (existente)
│   └── angular-frontend/           # ← NUEVO: Angular 18+
│       ├── src/app/
│       │   ├── components/         # Componentes reutilizables
│       │   ├── pages/              # Vistas/páginas
│       │   ├── services/           # Consumo de API
│       │   └── models/             # Interfaces TypeScript
│       ├── angular.json
│       ├── package.json
│       └── Dockerfile
```

---

## 🔄 FASE 0 — Preparación del Entorno y Discovery

> **Objetivo:** Dejar todo listo antes de escribir código. Configurar `.gitignore`, inicializar el `DEV_JOURNAL_ADR.md`, y verificar que el entorno de desarrollo funcione.

### 0.1 Configuración del `.gitignore`

- [ ] Agregar `.agents/` al `.gitignore` (requisito explícito del usuario)
- [ ] Verificar que el `.gitignore` cubra:
  ```
  .env
  node_modules/
  __pycache__/
  *.pyc
  dist/
  build/
  .venv/
  *.log
  .angular/
  .agents/
  ```
- [ ] Commit: `chore: update .gitignore — exclude .agents folder`

### 0.2 Inicialización del `DEV_JOURNAL_ADR.md`

- [ ] Crear el archivo `DEV_JOURNAL_ADR.md` en la raíz del proyecto
- [ ] Llenar las secciones iniciales:
  - Resumen Ejecutivo
  - Stack Tecnológico Elegido (con justificación)
  - Arquitectura del Sistema (textual + diagrama ASCII)
  - Modelo de Datos (5 entidades)
  - Contratos de API (endpoints planeados)
  - Supuestos tomados
  - Registro ADR-001: Elección de Angular como Frontend
- [ ] Commit: `docs: initialize DEV_JOURNAL_ADR.md for 3rd advance`

### 0.3 Verificación del Entorno de Desarrollo

- [ ] **Backend:** Verificar que el proyecto Spring Boot compila con `mvn clean compile`
- [ ] **Backend:** Confirmar conexión a Supabase PostgreSQL
- [ ] **Frontend:** Instalar Angular CLI global: `npm install -g @angular/cli@18`
- [ ] **Frontend:** Crear proyecto Angular en `web_project/angular-frontend/`
- [ ] **Frontend:** Verificar que `ng serve` levanta en `localhost:4200`
- [ ] **Python API:** Verificar que el microservicio Python corre sin errores
- [ ] Documentar versiones exactas en `DEV_JOURNAL_ADR.md`

---

## 🔧 FASE 1 — Backend: Completar las 5 Entidades y APIs REST

> **Objetivo:** Dejar el backend 100% funcional con CRUD completo para las 5 entidades, endpoints documentados en Swagger, envío de correos, y generación de PDFs.  
> **Subagente:** A (Backend) + D (QA)

### 1.1 Completar Entidades JPA

- [ ] **`User.java`** — Entidad JPA para `users`
  - Campos: `userId`, `name`, `email` (unique), `passwordHash`, `createdAt`
  - Anotaciones: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`
  - Validaciones: `@NotBlank`, `@Email`, `@Size`
- [ ] **`Dataset.java`** — Entidad JPA para `datasets`
  - Campos: `datasetId`, `name`, `description`, `datasetType`, `numSamples`, `numFeatures`, `targetVariable`, `createdAt`
- [ ] **`Model.java`** — Entidad JPA para `models`
  - Campos: `modelId`, `name`, `category`, `description`
- [ ] **`UploadedFile.java`** — Entidad JPA para `uploaded_files`
  - Campos: `fileId`, `fileName`, `storagePath`, `fileSize`, `uploadedAt`, `userId` (FK a users)
  - Relación: `@ManyToOne` con User
- [ ] Verificar que `Experiment.java` tiene todos los campos necesarios y relaciones correctas
- [ ] Ejecutar la aplicación y verificar que JPA crea/valida las tablas contra Supabase

### 1.2 Crear Repositorios JPA

- [ ] **`UserRepository.java`** — extiende `JpaRepository<User, Long>`
  - Métodos custom: `findByEmail(String email)`, `existsByEmail(String email)`
- [ ] **`DatasetRepository.java`** — extiende `JpaRepository<Dataset, Long>`
  - Métodos custom: `findByDatasetType(String type)`
- [ ] **`ModelRepository.java`** — extiende `JpaRepository<Model, Long>`
  - Métodos custom: `findByCategory(String category)`
- [ ] **`UploadedFileRepository.java`** — extiende `JpaRepository<UploadedFile, Long>`
  - Métodos custom: `findByUserId(Long userId)`
- [ ] **`ExperimentRepository.java`** — verificar el existente, agregar métodos faltantes
  - Métodos custom: `findByUserId(Long userId)`, `findByModelId(Long modelId)`

### 1.3 Implementar Servicios (Capa de Negocio)

- [ ] **`UserService.java`**
  - `createUser(User user)` — registrar nuevo usuario
  - `getUserById(Long id)` — obtener por ID
  - `getUserByEmail(String email)` — buscar por email
  - `getAllUsers()` — listar todos
  - `updateUser(Long id, User user)` — actualizar
  - `deleteUser(Long id)` — eliminar
  - `authenticate(String email, String password)` — login básico (hash de contraseña)
- [ ] **`DatasetService.java`**
  - CRUD completo: `create`, `getById`, `getAll`, `update`, `delete`
  - `getByType(String type)` — filtrar por tipo (linear, logistic, knn)
- [ ] **`ModelService.java`**
  - CRUD completo + `getByCategory(String category)`
- [ ] **`UploadedFileService.java`**
  - CRUD completo + `getByUserId(Long userId)`
  - `saveFileMetadata(MultipartFile file, Long userId)` — guardar metadatos de archivo
- [ ] **`ExperimentService.java`** — Revisar y completar el existente
  - Agregar `getByUserId()`, `getByModelId()`
- [ ] **`EmailService.java`** — Revisar el existente, asegurar que funciona con Gmail SMTP
  - Probar envío real con credenciales configuradas en `application.properties`
- [ ] **`PdfService.java`** — **NUEVO**: Generación de documentos PDF
  - Usar librería `iText` o `Apache PDFBox` (agregar dependencia a `pom.xml`)
  - Métodos:
    - `generateExperimentReport(Long experimentId)` — reporte de un experimento
    - `generateDatasetReport(Long datasetId)` — ficha técnica de dataset
    - `generateUserActivityReport(Long userId)` — historial de actividad
  - Estructura del PDF: encabezado IPN/ESCOM, título, datos en tabla, gráficas si aplica

### 1.4 Implementar Controladores REST

- [ ] **`UserController.java`**
  - `POST /api/users` — crear usuario
  - `GET /api/users` — listar todos
  - `GET /api/users/{id}` — obtener por ID
  - `PUT /api/users/{id}` — actualizar
  - `DELETE /api/users/{id}` — eliminar
  - `POST /api/users/login` — autenticación
- [ ] **`DatasetController.java`**
  - `POST /api/datasets` — crear
  - `GET /api/datasets` — listar todos
  - `GET /api/datasets/{id}` — obtener por ID
  - `PUT /api/datasets/{id}` — actualizar
  - `DELETE /api/datasets/{id}` — eliminar
  - `GET /api/datasets?type=linear` — filtrar por tipo
- [ ] **`ModelController.java`**
  - `POST /api/models` — crear
  - `GET /api/models` — listar todos
  - `GET /api/models/{id}` — obtener por ID
  - `PUT /api/models/{id}` — actualizar
  - `DELETE /api/models/{id}` — eliminar
- [ ] **`UploadedFileController.java`**
  - `POST /api/files/upload` — subir archivo (multipart)
  - `GET /api/files` — listar todos
  - `GET /api/files/{id}` — obtener metadatos
  - `GET /api/files/user/{userId}` — archivos de un usuario
  - `DELETE /api/files/{id}` — eliminar
- [ ] **`ExperimentController.java`** — Revisar existente, agregar endpoints:
  - `GET /api/experiments/user/{userId}` — experimentos por usuario
  - `GET /api/experiments/model/{modelId}` — experimentos por modelo
  - `POST /api/experiments/run` — ejecutar nuevo experimento (invoca Python API)
- [ ] **`EmailController.java`** — Revisar existente
  - `POST /api/email/send` — enviar correo personalizado
  - `POST /api/email/experiment-report/{id}` — enviar reporte por correo
- [ ] **`PdfController.java`** — **NUEVO**
  - `GET /api/pdf/experiment/{id}` — descargar PDF de experimento
  - `GET /api/pdf/dataset/{id}` — descargar PDF de dataset
  - `GET /api/pdf/user/{id}/activity` — descargar PDF de actividad
- [ ] **`MLController.java`** — Revisar existente (delega a Python API)
  - `POST /api/ml/linear` — regresión lineal
  - `POST /api/ml/logistic` — regresión logística
  - `POST /api/ml/knn` — K-Nearest Neighbors
- [ ] Agregar `@CrossOrigin(origins = "*")` en todos los controladores para permitir CORS desde Angular

### 1.5 Configuración y Dependencias

- [ ] Agregar dependencia de PDF al `pom.xml`:
  ```xml
  <dependency>
      <groupId>com.itextpdf</groupId>
      <artifactId>itext7-core</artifactId>
      <version>8.0.4</version>
      <type>pom</type>
  </dependency>
  ```
- [ ] Agregar dependencia para manejo de archivos (si no existe):
  ```xml
  <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.16.1</version>
  </dependency>
  ```
- [ ] Configurar `application.properties` con:
  - Conexión a Supabase PostgreSQL
  - Credenciales SMTP Gmail
  - URL de la Python API
  - Tamaño máximo de upload
  - Puerto del servidor

### 1.6 QA del Backend

- [ ] Verificar compilación: `mvn clean compile` sin errores
- [ ] Ejecutar la aplicación y verificar Swagger UI en `http://localhost:8080/swagger-ui/index.html`
- [ ] Probar cada endpoint con Swagger UI o curl
- [ ] Verificar que el envío de correos funciona (usar Mailtrap para pruebas)
- [ ] Verificar que la generación de PDF produce archivos válidos
- [ ] Verificar que la Python API es invocada correctamente desde `MLController`
- [ ] Commit: `feat: complete backend CRUD for all 5 entities + PDF + email`

---

## 🎨 FASE 2 — Frontend: Desarrollo Angular Completo

> **Objetivo:** Construir la SPA (Single Page Application) en Angular que consuma todos los endpoints del backend.  
> **Subagente:** B (Frontend) + D (QA)

### 2.1 Configuración Inicial del Proyecto Angular

- [ ] Crear proyecto: `ng new angular-frontend --style=css --routing=true --ssr=false`
- [ ] Instalar dependencias adicionales:
  - `@angular/material` — componentes UI
  - `chart.js` + `ng2-charts` — gráficas para resultados ML
  - `ngx-toastr` — notificaciones toast
- [ ] Configurar `environment.ts` y `environment.development.ts`:
  ```typescript
  export const environment = {
    production: false,
    apiUrl: 'http://localhost:8080/api'
  };
  ```
- [ ] Configurar proxy para desarrollo (`proxy.conf.json`) para evitar CORS:
  ```json
  {
    "/api": {
      "target": "http://localhost:8080",
      "secure": false
    }
  }
  ```
- [ ] Commit: `chore: initialize Angular project with dependencies`

### 2.2 Modelos TypeScript (Interfaces)

- [ ] `src/app/models/user.model.ts`
  ```typescript
  export interface User {
    userId: number;
    name: string;
    email: string;
    passwordHash?: string;
    createdAt: string;
  }
  ```
- [ ] `src/app/models/dataset.model.ts`
- [ ] `src/app/models/model.model.ts`
- [ ] `src/app/models/uploaded-file.model.ts`
- [ ] `src/app/models/experiment.model.ts`
- [ ] `src/app/models/ml-result.model.ts` — para resultados de ML
- [ ] `src/app/models/email.model.ts` — para envío de correos

### 2.3 Servicios HTTP (Capa de Consumo de API)

- [ ] **`api.service.ts`** — Servicio base con `HttpClient` configurado
  - URL base desde environment
  - Headers comunes
  - Manejo centralizado de errores
- [ ] **`user.service.ts`** — CRUD de usuarios + login
- [ ] **`dataset.service.ts`** — CRUD de datasets + filtro por tipo
- [ ] **`model.service.ts`** — CRUD de modelos ML
- [ ] **`file.service.ts`** — Upload/download de archivos
- [ ] **`experiment.service.ts`** — CRUD de experimentos + ejecución
- [ ] **`ml.service.ts`** — Consumo de endpoints ML (linear, logistic, knn)
- [ ] **`email.service.ts`** — Envío de correos
- [ ] **`pdf.service.ts`** — Descarga de PDFs generados
- [ ] Cada servicio debe manejar estados: loading, success, error

### 2.4 Componentes Compartidos (Reutilizables)

- [ ] **`navbar.component.ts`** — Barra de navegación superior
  - Links a todas las secciones
  - Indicador de ruta activa
  - Responsive (hamburger menu en móvil)
- [ ] **`footer.component.ts`** — Pie de página
  - Info del equipo, IPN/ESCOM
- [ ] **`loading-spinner.component.ts`** — Spinner de carga
- [ ] **`confirm-dialog.component.ts`** — Diálogo de confirmación genérico
- [ ] **`data-table.component.ts`** — Tabla de datos reutilizable
  - Columnas configurables
  - Ordenamiento
  - Paginación
  - Búsqueda/filtro
- [ ] **`empty-state.component.ts`** — Estado vacío ("No hay datos disponibles")
- [ ] **`error-message.component.ts`** — Mensaje de error genérico con reintento

### 2.5 Páginas/Vistas

#### 2.5.1 Módulo Home / Dashboard

- [ ] **`home.component.ts`** — Landing page
  - Título: "ML Lab — Laboratorio Interactivo de Machine Learning"
  - Descripción del proyecto (tomada del DEV_JOURNAL)
  - Tarjetas de acceso rápido a las secciones principales
  - Estadísticas generales (total experimentos, modelos, datasets)
- [ ] **`dashboard.component.ts`** — Dashboard general
  - Gráfica de experimentos por tipo de modelo (pastel/barras)
  - Últimos experimentos ejecutados
  - Datasets disponibles

#### 2.5.2 Módulo Usuarios

- [ ] **`user-list.component.ts`** — Lista de usuarios registrados
- [ ] **`user-form.component.ts`** — Formulario de registro/edición de usuario
- [ ] **`user-detail.component.ts`** — Vista de detalle de un usuario
- [ ] **`login.component.ts`** — Página de login
  - Formulario email + contraseña
  - Validación de campos
  - Redirección al dashboard tras login exitoso

#### 2.5.3 Módulo Datasets

- [ ] **`dataset-list.component.ts`** — Lista de datasets
  - Filtro por tipo (linear, logistic, knn)
  - Vista de tarjetas o tabla
- [ ] **`dataset-form.component.ts`** — Crear/editar dataset
- [ ] **`dataset-detail.component.ts`** — Vista detallada de un dataset
  - Metadatos completos
  - Botón para descargar PDF de ficha técnica

#### 2.5.4 Módulo Modelos ML

- [ ] **`model-list.component.ts`** — Catálogo de modelos
  - Agrupados por categoría
- [ ] **`model-form.component.ts`** — Registrar nuevo modelo
- [ ] **`model-detail.component.ts`** — Detalle de un modelo

#### 2.5.5 Módulo Experimentos

- [ ] **`experiment-list.component.ts`** — Historial de experimentos
  - Filtro por usuario, modelo, fecha
  - Indicador visual de estado (completado ✅, error ❌, ejecutando ⏳)
- [ ] **`experiment-form.component.ts`** — Configurar nuevo experimento
  - Seleccionar dataset
  - Seleccionar modelo ML
  - Configurar parámetros (hiperparámetros)
  - Botón "Ejecutar"
- [ ] **`experiment-detail.component.ts`** — Resultados de un experimento
  - Métricas (Accuracy, MSE, R²)
  - **Gráfica de resultados** (chart.js/ng2-charts)
  - Botón "Descargar PDF"
  - Botón "Enviar por correo"
- [ ] **`experiment-run.component.ts`** — Pantalla de ejecución en vivo
  - Indicador de progreso/loading mientras se ejecuta el experimento
  - Animación o mensaje de estado

#### 2.5.6 Módulo Archivos

- [ ] **`file-upload.component.ts`** — Subida de archivos
  - Drag & drop o selección
  - Barra de progreso
  - Validación de tipos (CSV, JSON, etc.)
  - Asociar a un usuario
- [ ] **`file-list.component.ts`** — Lista de archivos subidos
  - Con metadatos (nombre, tamaño, fecha, usuario)

#### 2.5.7 Módulo Resultados ML

- [ ] **`ml-linear.component.ts`** — Interfaz para Regresión Lineal
  - Formulario de entrada de datos
  - Visualización de resultados (recta de regresión)
  - Gráfica interactiva
- [ ] **`ml-logistic.component.ts`** — Interfaz para Regresión Logística
  - Formulario + resultados + curva sigmoide
- [ ] **`ml-knn.component.ts`** — Interfaz para KNN
  - Formulario + resultados + gráfica de clasificación

### 2.6 Enrutamiento

- [ ] Configurar `app.routes.ts` con todas las rutas:
  ```
  /                    → HomeComponent
  /dashboard           → DashboardComponent
  /login               → LoginComponent
  /users               → UserListComponent
  /users/new           → UserFormComponent
  /users/:id           → UserDetailComponent
  /users/:id/edit      → UserFormComponent
  /datasets            → DatasetListComponent
  /datasets/new        → DatasetFormComponent
  /datasets/:id        → DatasetDetailComponent
  /datasets/:id/edit   → DatasetFormComponent
  /models              → ModelListComponent
  /models/new          → ModelFormComponent
  /models/:id          → ModelDetailComponent
  /experiments         → ExperimentListComponent
  /experiments/new     → ExperimentFormComponent
  /experiments/:id     → ExperimentDetailComponent
  /experiments/run/:id → ExperimentRunComponent
  /files               → FileListComponent
  /files/upload        → FileUploadComponent
  /ml/linear           → MlLinearComponent
  /ml/logistic         → MlLogisticComponent
  /ml/knn              → MlKnnComponent
  ```
- [ ] Implementar **guards** de ruta (si aplica autenticación): `auth.guard.ts`

### 2.7 Estilo y Diseño

- [ ] Configurar tema de Angular Material (colores IPN: guinda y blanco)
- [ ] Crear hoja de estilos global (`styles.css`)
- [ ] Diseño **responsive** (escritorio, tablet, móvil)
- [ ] **SIN emojis** en la UI — diseño serio y profesional
- [ ] Consistencia visual entre todas las páginas
- [ ] Contraste mínimo WCAG AA (usar herramienta de verificación)
- [ ] Transiciones suaves entre rutas

### 2.8 QA del Frontend

- [ ] `ng build --configuration=production` sin errores
- [ ] `ng serve` — probar todas las rutas manualmente
- [ ] Verificar que todos los formularios validan correctamente
- [ ] Verificar mensajes de error cuando el backend no responde
- [ ] Verificar responsive design en Chrome DevTools (varios tamaños)
- [ ] Commit final del frontend: `feat: complete Angular frontend with all modules`

---

## 🔗 FASE 3 — Integración Backend ↔ Frontend (End-to-End)

> **Objetivo:** Conectar el frontend Angular con el backend Spring Boot y verificar flujos completos.  
> **Subagente:** D (QA) + Orquestador

### 3.1 Pruebas de Integración

- [ ] **Flujo 1: Registro y Login de Usuario**
  - Crear usuario desde Angular → verificar en BD → login → redirección
- [ ] **Flujo 2: Gestión de Datasets**
  - Crear dataset → listar → ver detalle → editar → eliminar
- [ ] **Flujo 3: Catálogo de Modelos**
  - Crear modelo ML → listar → ver detalle
- [ ] **Flujo 4: Subida de Archivos**
  - Subir archivo CSV → verificar metadatos → descargar
- [ ] **Flujo 5: Ejecución de Experimento Completo**
  - Seleccionar dataset + modelo → ejecutar → ver resultados con gráfica → descargar PDF → enviar por correo
- [ ] **Flujo 6: Módulo ML Directo**
  - Probar cada algoritmo (Linear, Logistic, KNN) desde la UI → ver resultados

### 3.2 Corrección de Issues

- [ ] Documentar todos los bugs encontrados durante la integración
- [ ] Corregir issues de CORS (asegurar `@CrossOrigin` en todos los controllers)
- [ ] Corregir discrepancias entre modelos TypeScript y respuestas del backend
- [ ] Ajustar formatos de fecha, números, y unidades
- [ ] Verificar que los PDFs se generan y descargan correctamente desde el frontend

### 3.3 Commit de Integración

- [ ] Commit: `feat: full-stack integration — Angular ↔ Spring Boot`

---

## 📄 FASE 4 — Documentación del Proyecto

> **Objetivo:** Producir toda la documentación requerida por el profesor.  
> **Subagente:** Orquestador (documentación) + D (QA de docs)

### 4.1 Diccionario de Datos

- [ ] Crear `Tercer_avance/Diccionario_de_Datos.md`
- [ ] Para cada una de las 5 entidades, documentar:
  - Nombre de la tabla
  - Descripción de la entidad
  - Para cada columna: nombre, tipo de dato, tamaño, nullable, default, descripción, restricciones
  - Llaves primarias y foráneas
  - Índices
- [ ] Incluir las tablas del microservicio Python si aplica

### 4.2 Modelo Entidad-Relación

- [ ] Crear diagrama E-R en formato imagen (PNG)
- [ ] Herramienta sugerida: **dbdiagram.io**, **draw.io**, o **Mermaid** exportado
- [ ] Incluir las 5 entidades con sus relaciones:
  ```
  users ──1:N──→ experiments
  users ──1:N──→ uploaded_files
  models ──1:N──→ experiments
  datasets ──1:N──→ experiments
  ```
- [ ] Guardar como `Tercer_avance/Modelo_ER.png`
- [ ] Incluir también versión Mermaid en el `README.md` para renderizado en GitHub

### 4.3 Script de Creación de Base de Datos

- [ ] Crear `Tercer_avance/Script_BD.sql`
- [ ] Debe incluir:
  - `CREATE TABLE` para las 5 entidades
  - Constraints (PK, FK, UNIQUE, NOT NULL, CHECK)
  - Índices en claves foráneas
  - `INSERT` de datos de ejemplo (3-5 registros por tabla)
  - Comentarios explicativos en cada sección
- [ ] Probar que el script corre contra PostgreSQL sin errores

### 4.4 Actualización del README.md

- [ ] Título del proyecto: "ML Lab — Laboratorio Interactivo de Machine Learning"
- [ ] Descripción del proyecto (1-2 párrafos)
- [ ] Stack tecnológico (Angular + Spring Boot + PostgreSQL + Python)
- [ ] Integrantes del equipo (4 personas)
- [ ] Estructura del repositorio
- [ ] Diagrama E-R (renderizado con Mermaid)
- [ ] Instrucciones para ejecutar localmente:
  - Backend: `mvn spring-boot:run`
  - Frontend: `npm install && ng serve`
  - Python API: `pip install -r requirements.txt && python app.py`
- [ ] URLs de despliegue (Frontend + Backend)
- [ ] Credenciales de prueba (si aplica)
- [ ] Endpoints principales (tabla resumen)

### 4.5 Actualización del `DEV_JOURNAL_ADR.md`

- [ ] Registrar todos los sprints completados con su `SPRINT_REPORT`
- [ ] Registrar problemas encontrados y soluciones
- [ ] Registrar decisiones arquitectónicas (ADRs)
- [ ] Sección de cierre con funcionalidades entregadas
- [ ] Lecciones aprendidas

---

## 🚀 FASE 5 — Despliegue y Entrega Final

> **Objetivo:** Desplegar frontend y backend, subir código a GitHub, entregar todo.  
> **Subagente:** D (DevOps) + Orquestador

### 5.1 Despliegue del Backend

- [ ] Verificar que el backend ya está desplegado en Render:
  - URL: `https://java-backend-74bx.onrender.com`
- [ ] Si no está activo, redeployar:
  - Conectar repositorio de GitHub a Render
  - Configurar `Dockerfile` para Spring Boot
  - Configurar variables de entorno (DB, SMTP, Python API URL)
  - Verificar que `/swagger-ui/index.html` carga correctamente
- [ ] Actualizar `application.properties` con URLs de producción

### 5.2 Despliegue del Frontend

- [ ] Elegir plataforma: **Netlify** (recomendada por el profesor)
- [ ] Build de producción: `ng build --configuration=production`
- [ ] Subir carpeta `dist/angular-frontend/browser/` a Netlify
  - Alternativa 1: Conectar repo de GitHub → deploy automático
  - Alternativa 2: CLI de Netlify (`netlify deploy --prod`)
- [ ] Configurar variable de entorno `apiUrl` apuntando al backend de Render
- [ ] Verificar que la URL de Netlify carga la app completa
- [ ] Configurar redirección SPA (`_redirects` o `netlify.toml`):
  ```
  /*    /index.html   200
  ```

### 5.3 Despliegue del Microservicio Python (si aplica)

- [ ] Verificar que la Python API está desplegada (o desplegar en Render/Railway)
- [ ] Configurar CORS en la Python API
- [ ] Actualizar `PythonApiService.java` con la URL de producción

### 5.4 Verificación Final de URLs

- [ ] **Frontend URL:** `https://[nombre].netlify.app` (o similar) — debe estar vivo
- [ ] **Backend URL:** `https://java-backend-74bx.onrender.com` (o similar) — debe responder
- [ ] **Swagger UI:** disponible públicamente
- [ ] **Repositorio GitHub:** público o con acceso para el profesor

### 5.5 Subida al Repositorio

- [ ] Asegurar que `.gitignore` excluye:
  - `node_modules/`
  - `.angular/`
  - `.env`
  - `.agents/`
  - `dist/`
  - `target/`
  - `__pycache__/`
- [ ] Commit final con todo el código:
  ```bash
  git add .
  git commit -m "feat: third advance delivery — full-stack ML Lab"
  git push origin main
  ```
- [ ] Crear tag de release: `git tag v3.0.0 && git push --tags`
- [ ] Actualizar el `DEV_JOURNAL_ADR.md` con la info de entrega final

### 5.6 Checklist de Entrega Final

- [ ] Código fuente completo del Backend en el repo
- [ ] Código fuente completo del Frontend en el repo
- [ ] `Diccionario_de_Datos.md` en `Tercer_avance/`
- [ ] `Modelo_ER.png` en `Tercer_avance/`
- [ ] `Script_BD.sql` en `Tercer_avance/`
- [ ] URL del Frontend funcional
- [ ] URL del Backend funcional (con Swagger)
- [ ] Envío de correos funcional (probar con cuenta real)
- [ ] Generación de PDF funcional (descargar desde la UI)
- [ ] `README.md` completo
- [ ] `DEV_JOURNAL_ADR.md` completo
- [ ] Liga del repositorio lista para entregar

---

## 📊 Resumen de Fases y Responsables

| Fase | Nombre | Subagente(s) | Prioridad | Depende de |
|------|--------|-------------|-----------|------------|
| F0 | Preparación y Discovery | Orquestador | 🔴 Crítica | — |
| F1 | Backend Completo | A (Backend) + D (QA) | 🔴 Crítica | F0 |
| F2 | Frontend Angular | B (Frontend) + D (QA) | 🔴 Crítica | F1 (contratos API) |
| F3 | Integración E2E | D (QA) + Orquestador | 🔴 Crítica | F1 + F2 |
| F4 | Documentación | Orquestador | 🟡 Alta | F1 + F2 |
| F5 | Despliegue y Entrega | D (DevOps) + Orquestador | 🔴 Crítica | F3 + F4 |

---

## ⚠️ Notas Importantes

1. **`.agents/` no se commitea.** Está en `.gitignore` desde la Fase 0.
2. **Sin emojis en la UI.** El diseño es profesional-académico, colores IPN (guinda #800000, blanco).
3. **El `DEV_JOURNAL_ADR.md` es la única fuente de verdad.** Todo cambio se registra ahí.
4. **Commits frecuentes.** Mínimo uno por tarea completada, siguiendo conventional commits (`feat:`, `fix:`, `docs:`, `chore:`).
5. **Pruebas después de cada fase.** No avanzar sin verificar que lo anterior funciona.
6. **El backend YA tiene 1 entidad implementada (Experiment).** Solo falta completar las otras 4 (User, Dataset, Model, UploadedFile).
7. **El microservicio Python YA funciona.** Solo verificar que la integración con Spring Boot sigue operativa.

---

> **Plan generado por:** Orquestador de la Software Factory Multi-Agente v2.0  
> **Fecha:** 25 de junio de 2026  
> **Archivo:** `Plan_Desarrollo_3er_Avance.md`
