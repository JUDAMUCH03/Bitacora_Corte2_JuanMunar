# Blue Velvet — Coctelería de Autor

- **Estudiante:** Juan David Munar
- **Materia:** Desarrollo y Operaciones de Software (DOSW)


## 1. Concepto e Identidad del Restaurante

- **Nombre Comercial:** Blue Velvet
- **Resumen:** Gastrobar contemporáneo de alta gama especializado en coctelería de autor, mixología botánica, destilados premium y mocktails artesanales
- **Propósito del Sistema:** Plataforma web transaccional orientada a digitalizar el ciclo de vida del servicio en sala y barra. Abarca consulta de carta digital interactiva, personalización rigurosa de tragos, comanda electrónica y proyección en tiempo real en barra mediante **KDS (Kitchen Display System)**, mitigando errores de comunicación y eliminando el uso de papel

## 2. Reglas de Negocio Críticas (Invariantes de Dominio)

### Destilado Base Obligatorio y Mutabilidad Controlada

- Todo cóctel con graduación alcohólica exige la selección mandatoria de su marca o tipo de destilado base (`spiritBrandId != null`).
- Se permite la mutación de la marca de destilado una única vez (`spiritModificationsCount < 1`), congelando y recalculando la tarifa final del ítem.
- **Condición de corte:** La modificación solo es válida mientras la orden permanezca en estado `RECIBIDO`. Una vez pasa a `EN PREPARACIÓN`, el trago queda estrictamente inmutable y cualquier intento de mutación retorna un código **HTTP 422 Unprocessable Entity**.

### Aislamiento Estricto de Mocktails (0.0% ABV)

- Toda bebida clasificada como `MOCKTAIL` tiene restringido cualquier modificador, bitter o licor que contenga graduación alcohólica (`isAlcoholic == true`).
- La validación se aplica a nivel de presentación y en la capa de servicios mediante guardas de dominio que rechazan la transacción con **HTTP 400 Bad Request** o **HTTP 422 Unprocessable Entity**.

### Restricción de Garnishes

- Cada cóctel permite un límite máximo estricto de hasta dos (2) garnishes o decoraciones botánicas complementarias.

### Unicidad de Comanda por Mesa

- Cada mesa física abierta en sala puede tener únicamente una (1) cuenta o comanda activa concurrente en estado no liquidado (`is_paid == false`).

### Máquina de Estados Unidireccional en KDS

- El flujo de la comanda sigue la secuencia estricta:

  `RECIBIDO` → `EN_PREPARACION` → `LISTO` → `ENTREGADO`

- La cancelación de pedidos solo es admisible cuando el estado es `RECIBIDO`; una vez iniciada la preparación en barra queda bloqueada toda anulación.

## 3. Funcionalidades y Tabla de Endpoints

| Funcionalidad | Método HTTP | Endpoint | Descripción |
| :--- | :---: | :--- | :--- |
| **Crear Ítem** | `POST` | `/api/v1/platos` | Registra un nuevo plato o cóctel aplicando validaciones Jakarta y de negocio (nombres únicos). |
| **Consultar Catálogo** | `GET` | `/api/v1/platos` | Retorna la lista completa del menú. |
| **Consultar por ID** | `GET` | `/api/v1/platos/{id}` | Busca y retorna un ítem específico; lanza 404 si no existe. |
| **Filtrar Disponibles**| `GET` | `/api/v1/platos/disponibles` | Retorna únicamente los ítems que se encuentran activos/en stock. |
| **Cambiar Estado** | `PUT` | `/api/v1/platos/{id}/disponibilidad` | Alterna el estado de disponibilidad (activo/inactivo) de un ítem. |

---

## 4. Diagramas de Arquitectura

### 4.1 Diagrama de Clases

Modela las entidades del dominio, sus atributos, relaciones y los servicios que orquestan las reglas de negocio.

<img width="757" height="827" alt="image" src="https://github.com/user-attachments/assets/02dc51ae-738a-42dc-b76b-eb4f190d2239" />

### 4.2 Diagrama de Componentes General

Vista macro de los componentes del sistema y su organización por capas, evidenciando el flujo de dependencias hacia el dominio.

<img width="780" height="946" alt="image" src="https://github.com/user-attachments/assets/af972482-e60a-4310-89bf-2e2abd65ce58" />

### 4.3 Diagrama de Componentes Específicos

Detalle de los componentes del **módulo de órdenes y comandas**, que concentra las reglas de negocio críticas (mutabilidad de destilado, garnishes, máquina de estados y unicidad de comanda).

<img width="838" height="844" alt="image" src="https://github.com/user-attachments/assets/b6013214-f1f2-4163-a891-9ba3c9ed5658" />

### 4.4 Diagrama de Secuencia

Representa el flujo completo de **creación de una orden, personalización de un ítem con destilado, mutación del destilado y avance del estado en el KDS**, incluyendo las validaciones de las invariantes de dominio.

<img width="1677" height="940" alt="image" src="https://github.com/user-attachments/assets/e9e0ed7b-623c-49b2-ae08-d9f0001c2c3f" />

### 5. Métricas de Calidad y Pruebas Unitarias

#### 5.1 Reporte de Cobertura con JaCoCo
Se implementaron pruebas unitarias puras con JUnit 5 y Mockito aislando la lógica de negocio y validadores, alcanzando una cobertura global superior al 85% y un 99% en la capa de servicios transaccionales.

<img width="1181" height="216" alt="Captura de pantalla 2026-09-21 184824" src="https://github.com/user-attachments/assets/663706bd-c3e5-4e11-83ff-5759fb8d47dc" />

#### 5.2 Análisis Estático de Código con SonarQube
El escaneo estático validó la ausencia de vulnerabilidades de seguridad, cero deuda técnica crítica y cumplimiento total del Quality Gate.

<img width="1658" height="802" alt="Captura de pantalla 2026-09-21 190810" src="https://github.com/user-attachments/assets/50a500ae-2b04-436f-bd64-510d98f074b1" />

---

### 6. Verificación de Contratos y Respuestas HTTP (Swagger UI)

#### 6.1 Creación Exitosa (HTTP 201 Created)
<img width="1405" height="700" alt="Captura de pantalla 2026-09-21 181344" src="https://github.com/user-attachments/assets/a97058d4-1a89-4025-b080-08bb648d30c2" />

#### 6.2 Validación de Entrada (HTTP 400 Bad Request)
<img width="1424" height="616" alt="Captura de pantalla 2026-09-21 181416" src="https://github.com/user-attachments/assets/d2753d11-b81e-4445-864f-00c391570684" />

#### 6.3 Conflicto de Negocio por Duplicado (HTTP 409 Conflict)
<img width="1417" height="698" alt="Captura de pantalla 2026-09-21 181457" src="https://github.com/user-attachments/assets/469f2c1b-6d18-4233-b45f-1656cd24e0a0" />

