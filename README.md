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

## 3. Diagramas de Arquitectura

### 3.1 Diagrama de Clases

Modela las entidades del dominio, sus atributos, relaciones y los servicios que orquestan las reglas de negocio.

<img width="757" height="827" alt="image" src="https://github.com/user-attachments/assets/02dc51ae-738a-42dc-b76b-eb4f190d2239" />

### 3.2 Diagrama de Componentes General

Vista macro de los componentes del sistema y su organización por capas, evidenciando el flujo de dependencias hacia el dominio.

<img width="780" height="946" alt="image" src="https://github.com/user-attachments/assets/af972482-e60a-4310-89bf-2e2abd65ce58" />

### 3.3 Diagrama de Componentes Específicos

Detalle de los componentes del **módulo de órdenes y comandas**, que concentra las reglas de negocio críticas (mutabilidad de destilado, garnishes, máquina de estados y unicidad de comanda).

<img width="838" height="844" alt="image" src="https://github.com/user-attachments/assets/b6013214-f1f2-4163-a891-9ba3c9ed5658" />

### 3.4 Diagrama de Secuencia

Representa el flujo completo de **creación de una orden, personalización de un ítem con destilado, mutación del destilado y avance del estado en el KDS**, incluyendo las validaciones de las invariantes de dominio.

<img width="1677" height="940" alt="image" src="https://github.com/user-attachments/assets/e9e0ed7b-623c-49b2-ae08-d9f0001c2c3f" />
