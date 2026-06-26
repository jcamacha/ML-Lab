# DEV_JOURNAL_ADR — [NOMBRE DEL SISTEMA]
> Architecture Decision Record & Development Journal
> Generado por: agy Software Factory v2.0
> Proyecto iniciado: [FECHA]

---

## RESUMEN EJECUTIVO

[Descripción del sistema: qué es, qué problema resuelve, quién lo usa]

---

## STACK TECNOLÓGICO

| Capa             | Tecnología | Versión | Razón de elección |
|------------------|------------|---------|-------------------|
| Backend          |            |         |                   |
| Base de datos    |            |         |                   |
| Frontend         |            |         | N/A si no aplica  |
| Infraestructura  |            |         |                   |
| Testing          |            |         |                   |

---

## ARQUITECTURA DEL SISTEMA

### Diagrama de componentes
```
[Diagrama ASCII de la arquitectura]
```

### Descripción del flujo principal
[Cómo fluye la información de extremo a extremo]

---

## MODELO DE DATOS

### Entidades principales

**Entidad: [Nombre]**
| Campo       | Tipo    | Restricciones | Descripción |
|-------------|---------|---------------|-------------|
| id          | UUID/INT| PK, NOT NULL  |             |
| created_at  | DATETIME| NOT NULL      |             |
| updated_at  | DATETIME| NOT NULL      |             |

### Relaciones
[Descripción de las relaciones entre entidades]

---

## CONTRATOS DE API

### [MÉTODO] /api/[ruta]
- **Propósito:** [qué hace]
- **Autenticación requerida:** Sí/No
- **Request body:**
  ```json
  { }
  ```
- **Response exitosa (200):**
  ```json
  { "data": {}, "meta": { "timestamp": "" } }
  ```
- **Errores posibles:** 400, 401, 404, 500

---

## SUPUESTOS TOMADOS

- **SUPUESTO-1:** [descripción] — Razón: [por qué se asumió sin preguntar]

---

## REGISTRO DE DECISIONES ARQUITECTÓNICAS (ADR)

### ADR-001: [Título de la primera decisión]
- **Fecha:** [fecha]
- **Estado:** APROBADA
- **Contexto:** [situación que requirió tomar una decisión]
- **Opciones consideradas:**
  1. [Opción A] — Pros: [...] Contras: [...]
  2. [Opción B] — Pros: [...] Contras: [...]
- **Decisión:** [qué se eligió y por qué]
- **Consecuencias:** [qué implica esta decisión a futuro]

---

## REGISTRO DE SPRINTS

### Sprint 0: Infraestructura base — [ESTADO]
- Fecha: [fecha]
- Subagente: D (DevOps/QA)
- Archivos producidos: [lista]
- Observaciones: [notas relevantes]

---

## REGISTRO DE PROBLEMAS

### PROBLEMA-001: [Título descriptivo]
- Fecha: [fecha]
- Sprint afectado: [número]
- Subagente que lo encontró: [letra]
- Descripción técnica: [qué falló exactamente]
- Log del error:
  ```
  [error log]
  ```
- Solución aplicada: [cómo se resolvió]
- ADR generado: [ADR-N si el problema requirió una decisión arquitectónica]

---

## DEUDA TÉCNICA

| ID   | Descripción | Prioridad | Sprint donde surgió |
|------|-------------|-----------|---------------------|
| DT-1 |             | Alta/Media/Baja |               |

---

## CIERRE DEL PROYECTO

> Esta sección se completa en la Fase 5 — Cierre

- **Fecha de cierre:** [fecha]
- **Estado:** [ENTREGADO / ENTREGADO CON DEUDA TÉCNICA]

### Funcionalidades entregadas
- [x] [funcionalidad]

### Deuda técnica pendiente
- [ ] [descripción] — Prioridad: [Alta/Media/Baja]

### Lecciones aprendidas
- [observación para futuras sesiones del factory]

---

## NOTAS PARA EL AGENTE LATEX

*Para quien genere la documentación final de este sistema:*

| Sección del reporte | Fuente en este journal |
|---------------------|------------------------|
| Introducción        | RESUMEN EJECUTIVO      |
| Marco teórico       | STACK TECNOLÓGICO + ADR |
| Desarrollo          | REGISTRO DE SPRINTS    |
| Problemas           | REGISTRO DE PROBLEMAS  |
| Conclusiones        | CIERRE + DEUDA TÉCNICA |
| Bibliografía        | Links en STACK y ADR   |
