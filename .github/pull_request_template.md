## Resumen del cambio
Describe brevemente que problema resuelve este PR y el enfoque aplicado.

## Tipo de cambio
- [ ] `feat` (nueva funcionalidad)
- [ ] `fix` (correccion)
- [ ] `refactor`
- [ ] `docs`
- [ ] `test`
- [ ] `chore`

## Contexto funcional
- Ticket/Issue relacionado: #
- Criterios de aceptacion cubiertos:
  - [ ] CA-1
  - [ ] CA-2

## Checklist tecnico (obligatorio)
- [ ] Respeta arquitectura por capas (`controller -> service -> dao -> model -> mapper -> dto`).
- [ ] Endpoints versionados en `/v1/<recurso>` (si aplica).
- [ ] DTOs usados en request/response (sin exponer entidades JPA).
- [ ] Validaciones de entrada agregadas/actualizadas (`@Valid`, Bean Validation).
- [ ] Manejo de errores consistente (`ControllerAdvice` y excepciones de dominio).
- [ ] Documentacion OpenAPI actualizada (si cambia API).
- [ ] Sin secretos hardcodeados ni datos sensibles.

## Pruebas
- [ ] Pruebas unitarias
- [ ] Pruebas de integracion
- [ ] Prueba manual

### Evidencia de pruebas
Incluye comandos y resultados relevantes.

```text
# ejemplo
mvn -q test
```

## Riesgos y mitigaciones
- Riesgo principal:
- Mitigacion aplicada:
- Plan de rollback:

## Impacto en despliegue
- [ ] Sin cambios de infraestructura
- [ ] Requiere variables de entorno nuevas
- [ ] Requiere migracion de base de datos
- [ ] Requiere coordinacion con otros servicios

## Notas para reviewer
Puntos especificos donde enfocar la revision.

