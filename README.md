# agy Software Factory v2.0
## Framework Multi-Agente para Desarrollo de Sistemas

---

## ¿Qué es esto?

Un framework que convierte a `agy` en una fábrica de software multi-agente.
Un Orquestador (Director de Proyecto) analiza los requerimientos, diseña la
arquitectura, y delega la implementación a subagentes especializados, mientras
documenta todo el proceso para la generación posterior de reportes académicos.

---

## Estructura del framework

```
.agents/
├── agents.md                        ← Definición del equipo y roles
└── skills/
    ├── 01_orquestador_metodologia.md ← Proceso completo del Orquestador
    ├── 02_subagente_backend.md       ← Estándares del Ingeniero Backend
    ├── 03_subagente_frontend.md      ← Estándares del Ingeniero Frontend
    ├── 04_subagente_devops_qa.md     ← Protocolo de QA y DevOps
    └── 05_analista_legacy.md        ← Análisis de sistemas existentes

DEV_JOURNAL_ADR_template.md          ← Template del journal de proyecto
README.md                            ← Este archivo
```

---

## Instalación

### Opción A — Global (disponible en todos los proyectos)
```bash
cp -r .agents/ ~/.gemini/antigravity/
```

### Opción B — Por proyecto (recomendada para aislar contexto)
```bash
# Copia la carpeta .agents/ a la raíz de tu proyecto
cp -r .agents/ /ruta/a/tu/proyecto/
cp DEV_JOURNAL_ADR_template.md /ruta/a/tu/proyecto/
```

---

## Uso: Proyecto nuevo

### 1. Prepara la carpeta del proyecto
```bash
mkdir Mi_Nuevo_Sistema
cd Mi_Nuevo_Sistema
cp -r /ruta/al/factory/.agents/ .
cp /ruta/al/factory/DEV_JOURNAL_ADR_template.md .
```

### 2. Abre agy con los MCPs necesarios
```bash
# Mínimo recomendado para cualquier proyecto:
agy \
  --mcp "npx -y @modelcontextprotocol/server-sequential-thinking" \
  --mcp "npx -y @modelcontextprotocol/server-brave-search"

# Para proyectos con base de datos Supabase:
agy \
  --mcp "npx -y @modelcontextprotocol/server-sequential-thinking" \
  --mcp "npx -y @modelcontextprotocol/server-brave-search" \
  --mcp "npx -y @supabase/mcp-server-supabase --access-token TU_TOKEN"
```

### 3. Prompt inicial para proyecto nuevo
```
Hola Arquitecto. Aquí están los requerimientos del sistema que necesito construir:

[DESCRIPCIÓN DEL SISTEMA]
[REQUERIMIENTOS FUNCIONALES]
[RESTRICCIONES TÉCNICAS si las hay]
[FECHA LÍMITE si aplica]

Lee tu skill de metodología y comienza la Fase 0.
Dime qué MCPs adicionales necesitas y cuáles son tus dudas
bloqueantes antes de continuar.
```

---

## Uso: Migración o refactorización de sistema existente

### 1. Prepara el handoff del sistema legacy
```bash
mkdir Mi_Sistema_Migrado
cd Mi_Sistema_Migrado
cp -r /ruta/al/factory/.agents/ .
mkdir legacy_docs
# Copia los archivos clave del sistema antiguo a legacy_docs/
# O copia el LEGACY_HANDOFF.md si ya existe
```

### 2. Prompt inicial para migración
```
Hola Arquitecto. Necesito migrar/refactorizar un sistema existente.

OBJETIVO DE LA MIGRACIÓN: [qué quieres lograr]
RESTRICCIONES: [qué debe conservarse, qué puede cambiarse]

En la carpeta legacy_docs/ dejé [los archivos del sistema antiguo /
el LEGACY_HANDOFF.md ya generado].

Si no hay LEGACY_HANDOFF.md, invoca primero al Subagente E para
que analice el sistema. Luego procede con la Fase 0 de tu metodología.
```

---

## Flujo completo

```
ENTRADA
  ├── Requerimientos (texto) → Proyecto nuevo
  └── Sistema legacy + objetivo → Migración/Refactorización

FASE 0: Clasificación y MCPs
  └── ¿Qué tipo de sistema? ¿Qué MCPs necesito?

FASE 1: Discovery
  └── Extracción de requerimientos + preguntas bloqueantes al usuario

FASE 2: Arquitectura
  └── Investigación → Blueprint → DEV_JOURNAL_ADR.md → Aprobación usuario

FASE 3: Bootstrapping
  └── Skills locales de subagentes + estructura de carpetas + git init

FASE 4: Sprints
  └── Orquestador → SPRINT_BRIEF → Subagente → SPRINT_REPORT
      → QA → [DONE ✅ | FALLIDO ❌ → corregir → retry]

FASE 5: Cierre
  └── QA final → Cierre del journal → Commit → Listo para Agente LaTeX

SALIDA
  ├── Sistema funcionando
  └── DEV_JOURNAL_ADR.md (input para reportes académicos LaTeX)
```

---

## Conexión con el flujo LaTeX

El `DEV_JOURNAL_ADR.md` que produce este factory es el input directo
para el Agente Obrero del flujo de reportes LaTeX.

```bash
# Al terminar el proyecto, usa el journal como resumen de arquitectura:
cp DEV_JOURNAL_ADR.md /ruta/al/reporte_latex/resumen_arquitectura.md
```

El journal ya incluye una sección "NOTAS PARA EL AGENTE LATEX" que
mapea cada sección del journal con la sección del reporte académico.

---

## Subagentes disponibles

| Subagente | Cuándo se activa                                    |
|-----------|-----------------------------------------------------|
| A Backend | El sistema tiene API, lógica de negocio, o BD       |
| B Frontend| El sistema tiene interfaz visual para usuarios      |
| C Datos   | El sistema tiene ML, pipelines de datos, o análisis |
| D QA      | **Siempre** — en todos los proyectos                |
| E Legacy  | El proyecto es migración o refactorización          |
