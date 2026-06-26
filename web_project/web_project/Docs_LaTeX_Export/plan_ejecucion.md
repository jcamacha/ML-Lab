# Plan de Ejecución — ML Lab Reporte LaTeX

- **Modo**: Rápido (sin confirmación del usuario en puntos de control intermedios)
- **Nivel de Profundidad**: Estándar
- **Fases a ejecutar**:
  - **Fase 1: Estructura y Portada**: Configuración de carpetas del proyecto LaTeX, archivos auxiliares (`.vscode/settings.json`, `.gitignore`), carga de logotipos institucionales del IPN y la ESCOM, y esqueleto principal de `main.tex` con metadatos y accesibilidad PDF/UA-2.
  - **Fase 2: Investigación y Teoría**: Redacción académica de las secciones teóricas (`01_objetivo.tex`, `02_introduccion.tex`) y referencias bibliográficas (`05_fuentes.tex`) cubriendo microservicios, Spring Boot, Angular, FastAPI y Apache PDFBox.
  - **Fase 3: Integración de Código**: Incorporación de fragmentos de código relevantes en `03_desarrollo.tex` utilizando `\lstinputlisting` desde `codigos/`, explicaciones formales, capturas comentadas y redacción de conclusiones (`04_conclusiones.tex`).
  - **Fase 4: Compilación y QA**: Compilación iterativa mediante LuaLaTeX con dos pasadas para generar `main.pdf`, diagnóstico de errores, verificación de accesibilidad y registro de mejoras.
