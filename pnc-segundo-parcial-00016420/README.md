# Segundo Parcial – Sistema de Gestión de Clínica Veterinaria

## Indicaciones Generales

Lea detenidamente el enunciado y asegúrese de comprender los requisitos funcionales, las reglas de negocio y los entregables antes de comenzar. El ejercicio debe resolverse **individual**, aplicando correctamente la **arquitectura N-Capas** y las buenas prácticas vistas en clase.

> ⚠️ **No se aceptarán commits realizados después de la hora límite establecida para el parcial.**

---

## Sistema de Gestión de Clínica Veterinaria

**Descripción:** Desarrollar una API REST que permita gestionar los medicamentos e insumos del inventario de una clínica veterinaria, implementando las operaciones básicas de CRUD, validaciones, reglas de negocio y manejo adecuado de respuestas HTTP.

---

## Entidad `Product`

| Campo | Tipo | Reglas |
|-------|------|--------|
| `id` | Long | Autogenerado |
| `name` | String | Requerido, único, no vacío |
| `description` | String | Opcional |
| `category` | Enum | Uno de: `MEDICINE`, `VACCINE`, `SUPPLEMENT`, `SURGICAL_SUPPLY`, `FOOD` |
| `price` | BigDecimal | Obligatorio, mayor a 0 |
| `stock` | Integer | Obligatorio, ≥ 0 |
| `available` | Boolean | Se gestiona según stock |
| `requiresPrescription` | Boolean | `true` si la categoría es `MEDICINE` o `VACCINE` |
| `expirationDate` | Date | Obligatorio, debe ser una fecha futura |
| `supplier` | String | Requerido, no vacío |

---

## Reglas de Negocio

- **Nombre único:** No se permite registrar dos productos con el mismo nombre sin importar mayúsculas/minúsculas. No puede existir un producto sin nombre ni category.
- **Precio válido:** El precio debe ser mayor a cero. No se permiten productos gratuitos.
- **Stock y disponibilidad:** Si `stock = 0`, el campo `available` debe cambiar automáticamente a `false`. Si `stock > 0`, se puede cambiar `available` a `true` manualmente.
- **Actualización de stock:** No se permite que el stock resultante sea menor que 0 tras cualquier operación de descuento.
- **Fecha de vencimiento:** La `expirationDate` debe ser posterior a la fecha actual; de lo contrario, lanzar excepción.
- **Protección de eliminación:** No se puede eliminar un producto con categoría `VACCINE` si `available = true`, ya que puede haber campañas activas.
- **Filtrado:** El listado debe soportar filtros por `category` y `available`.
    - Ejemplo: `GET /api/products?category=VACCINE&available=true`

---

## Operaciones CRUD

| Método HTTP | Endpoint | Descripción |
|-------------|----------|-------------|
| `POST` | `/api/products` | Registrar un nuevo producto |
| `GET` | `/api/products` | Listar todos los productos (con filtros opcionales) |
| `GET` | `/api/products/{id}` | Obtener un producto por ID |
| `PUT` | `/api/products/{id}` | Actualizar la información de un producto |
| `DELETE` | `/api/products/{id}` | Eliminar un producto (respetando reglas de negocio) |

---

## Manejo de Excepciones

Se requieren **al menos 2 excepciones personalizadas**, por ejemplo:

- `ResourceNotFoundException` → HTTP `404`
- `BusinessRuleException` → HTTP `400`

Deben manejarse con un `@RestControllerAdvice` global.

---

## Códigos de Estado HTTP Esperados

| Situación | Código |
|-----------|--------|
| Creación exitosa | `201 Created` |
| Consulta exitosa | `200 OK` |
| Recurso no encontrado | `404 Not Found` |
| Datos inválidos / regla de negocio violada | `400 Bad Request` |
| Eliminación exitosa | `200 OK` o `204 No Content` |

---

## Entregables

Repositorio en **GitHub** con el nombre:

```
pnc-segundo-parcial-<carnet_estudiante>
```

El repositorio debe contener:

1. Código fuente del proyecto Spring Boot.

---

## Rúbrica de Evaluación (90% práctica)

| Criterio | Porcentaje |
|----------|-----------|
| CRUD completo y funcional | 22% |
| Reglas de negocio implementadas correctamente | 23% |
| Uso de anotaciones, entities, DTOs y validaciones | 15% |
| Estructura en capas y claridad del código | 10% |
| Manejo de excepciones personalizado (mínimo 2) | 10% |
| Conexión a base de datos | 5% |
| Buen manejo de códigos de estado HTTP (404, 400, 201, etc.) | 5% |
| **Total** | **90%** |

> El **10% restante** de la nota del parcial corresponde a la parte teórica, evaluada por separado.