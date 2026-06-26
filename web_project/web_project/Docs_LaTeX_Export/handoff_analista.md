# Handoff del Analista de Código — ML Lab

Este documento proporciona los hallazgos principales y la estructura del código escaneado en el proyecto final, sirviendo de puente para la redacción de la sección de Desarrollo del reporte académico LaTeX.

## Resumen del Análisis

El escaneo de las tres carpetas principales del proyecto (`java-backend/`, `python-api/`, `angular-frontend/`) revela una implementación robusta de una aplicación orientada a servicios de analítica de datos:

1. **Backend en Spring Boot (`java-backend`)**:
   - **Controladores**: Controladores desacoplados de persistencia que manejan solicitudes HTTP, validando parámetros y formateando cabeceras de respuesta.
   - **Servicios**:
     - `PdfService.java`: Utiliza Apache PDFBox para dibujar coordenadas de cabeceras, líneas y texto directamente sobre el canvas del PDF, previniendo caracteres rotos al procesar texto.
     - `PythonApiService.java`: Realiza solicitudes de red (`RestTemplate`) al microservicio de Python desplegado en Render (`https://python-api-vj5u.onrender.com/`). Cuando el microservicio de Python responde con métricas, el servicio deserializa el JSON con Jackson y almacena una instancia en el repositorio de experimentos.
   - **Persistencia**: Se detectaron entidades `@Entity` mapeadas a tablas PostgreSQL en Supabase.

2. **Microservicio ML en FastAPI (`python-api`)**:
   - Servidor FastAPI modular en `app.py` que incluye enrutadores específicos para algoritmos en `routes/`.
   - `models/knn.py` carga datasets locales (a través de un Dataset Manager), entrena un clasificador `KNeighborsClassifier` de scikit-learn, calcula el `accuracy_score` de entrenamiento, y realiza predicciones puntuales.

3. **Frontend en Angular 18+ (`angular-frontend`)**:
   - Estructura moderna de componentes independientes (`standalone: true`).
   - `MlService` hereda de una clase base `ApiService` para mantener uniforme la gestión de URL base y el manejo de errores HTTP observables.
   - Los componentes capturan datos en cadenas JSON, las parsean localmente en matrices y vectores y realizan las peticiones, gestionando estados visuales como cargando (`isLoading`) y errores en pantalla.
