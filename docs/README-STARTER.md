# README Starter - Onboarding rapido

## Objetivo
Este documento acelera la incorporacion de nuevos desarrolladores al microservicio.

## 1) Que debes entender primero
- Arquitectura por capas: `controller -> service -> dao -> model -> mapper -> dto`.
- Convencion de endpoints: `/v1/<recurso>`.
- Respuestas por DTO (no exponer entidades).
- Manejo de errores centralizado con `ControllerAdvice`.

## 2) Estructura clave del codigo
- `src/main/java/.../controller`: endpoints REST.
- `src/main/java/.../service`: logica de negocio.
- `src/main/java/.../dao`: repositorios JPA.
- `src/main/java/.../model`: entidades.
- `src/main/java/.../mapper`: conversiones MapStruct.
- `src/main/java/.../dto`: request/response.
- `src/test/java`: pruebas.

## 3) Flujo de trabajo recomendado
1. Crear DTO request/response.
2. Crear entidad + DAO.
3. Crear contrato de servicio + implementacion.
4. Crear mapper.
5. Exponer controller con validaciones.
6. Agregar pruebas minimas.
7. Documentar endpoint (OpenAPI).

## 4) Checklist rapido antes de PR
- [ ] Compila y tests OK.
- [ ] Endpoints versionados en `/v1`.
- [ ] Validaciones de entrada completas.
- [ ] Sin secretos hardcodeados.
- [ ] Logs utiles para soporte.
- [ ] Errores mapeados de forma consistente.

## 5) Do/Don't para PR reviews

### Do
- Revisar primero riesgos funcionales: regresiones, seguridad, contratos API y persistencia.
- Verificar que se respete la arquitectura por capas y nomenclatura estandar.
- Pedir evidencia de pruebas (unitarias/integracion) y revisar casos borde.
- Confirmar que no se exponen entidades JPA en respuestas y que hay DTO + mapper.
- Validar que cambios de endpoint incluyan documentacion OpenAPI y codigos HTTP correctos.

### Don't
- No aprobar PR sin contexto funcional, criterios de aceptacion o plan de prueba.
- No mezclar refactor grande con cambio funcional sin justificar alcance.
- No aceptar secretos, credenciales o datos sensibles en codigo/config versionada.
- No ignorar manejo de errores y trazabilidad (logs insuficientes o ambiguos).
- No dejar comentarios vagos; registrar feedback accionable con ubicacion y motivo.

## 6) Prompt corto para IA
```text
Genera un modulo CRUD en Spring Boot con arquitectura por capas (controller, service, dao, model, mapper, dto),
endpoints /v1/<recurso>, DTOs con validaciones, manejo de errores con ControllerAdvice,
y pruebas minimas de controller/service. No exponer entidades JPA en respuestas.
```

## 7) Lectura siguiente
- Guia completa: `ARCHITECTURE.md`
- Referencia CRUD canonica: seccion "Ejemplo canonico CRUD completo" en `ARCHITECTURE.md`
- Template PR: `.github/pull_request_template.md`
- Templates de issue: `.github/ISSUE_TEMPLATE/`
