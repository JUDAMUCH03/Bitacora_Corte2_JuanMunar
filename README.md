# Blue Velvet — Coctelería de Autor

- **Estudiante:** Juan David Munar
- **Materia:** Desarrollo y Operaciones de Software (DOSW)

---

## 1. Concepto e Identidad del Restaurante

<img width="1254" height="1254" alt="LogoBlueVelvet" src="https://github.com/user-attachments/assets/ce990092-9dd6-419f-b605-01870fdf20ed" />

- **Nombre Comercial:** Blue Velvet
- **Resumen:** Gastrobar contemporáneo de alta gama especializado en coctelería de autor, mixología botánica, destilados premium y mocktails artesanales.
- **Propósito del Sistema:** Plataforma web transaccional orientada a digitalizar el ciclo de vida del servicio en sala y barra. Abarca consulta de carta digital interactiva, personalización rigurosa de tragos, comanda electrónica y proyección en tiempo real en barra mediante **KDS (Kitchen Display System)**, mitigando errores de comunicación y eliminando el uso de papel.

---

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

---

## 3. Funcionalidades y Tabla de Endpoints

| Funcionalidad | Método HTTP | Endpoint | Descripción |
| :--- | :---: | :--- | :--- |
| **Crear Ítem** | `POST` | `/api/v1/platos` | Registra un nuevo plato o cóctel aplicando validaciones Jakarta y de negocio (nombres únicos). |
| **Consultar Catálogo** | `GET` | `/api/v1/platos` | Retorna la lista completa del menú. |
| **Consultar por ID** | `GET` | `/api/v1/platos/{id}` | Busca y retorna un ítem específico; lanza 404 si no existe. |
| **Filtrar Disponibles**| `GET` | `/api/v1/platos/disponibles` | Retorna únicamente los ítems que se encuentran activos/en stock. |
| **Cambiar Estado** | `PATCH` | `/api/v1/platos/{id}/disponible` | Alterna el estado de disponibilidad (activo/inactivo) de un ítem. |
| **Actualizar Ítem** | `PUT` | `/api/v1/platos/{id}` | Actualiza datos y parámetros de carta de un ítem existente. |
| **Eliminar Ítem** | `DELETE` | `/api/v1/platos/{id}` | Elimina un plato del catálogo de persistencia. |

---

## 4. Diagramas de Arquitectura

### 4.1 Diagrama de Clases
Modela las entidades del dominio, sus atributos, relaciones y los servicios que orquestan las reglas de negocio.

<img width="940" height="811" alt="clasesBV" src="https://github.com/user-attachments/assets/ad9c7fca-915c-4bea-9cb4-52563710c925" />

### 4.2 Diagrama de Componentes General
Vista macro de los componentes del sistema y su organización por capas, evidenciando el flujo de dependencias hacia el dominio.

<img width="836" height="389" alt="Captura de pantalla 2026-10-08 173023" src="https://github.com/user-attachments/assets/70e13592-4b60-4751-a25a-79f207e8f099" />

### 4.3 Diagrama de Componentes Específicos
Vista detallada del dominio.

<img width="1519" height="1062" alt="Comp" src="https://github.com/user-attachments/assets/236a6017-54e6-49c4-8405-f9ad8d864667" />

### 4.4 Diagrama de Contexto C4
<img width="992" height="582" alt="C4" src="https://github.com/user-attachments/assets/a851d19c-fefc-47e8-a7d2-119e6e460c94" />

---

## 5. Métricas de Calidad y Pruebas Unitarias

### 5.1 Reporte de Cobertura con JaCoCo
Pruebas automatizadas con JUnit 5 y Mockito aislando la lógica de negocio y validadores, alcanzando una cobertura global superior al 85% y 99% en servicios transaccionales.

<img width="1181" height="216" alt="Captura de pantalla 2026-09-21 184824" src="https://github.com/user-attachments/assets/663706bd-c3e5-4e11-83ff-5759fb8d47dc" />

### 5.2 Análisis Estático de Código con SonarQube
Validación de ausencia de vulnerabilidades de seguridad, cero deuda técnica crítica y cumplimiento de Quality Gate.

<img width="1658" height="802" alt="Captura de pantalla 2026-09-21 190810" src="https://github.com/user-attachments/assets/50a500ae-2b04-436f-bd64-510d98f074b1" />

---

## 6. Verificación de Contratos y Respuestas HTTP (Swagger UI)

### 6.1 Creación Exitosa (HTTP 201 Created)
<img width="1405" height="700" alt="Captura de pantalla 2026-09-21 181344" src="https://github.com/user-attachments/assets/a97058d4-1a89-4025-b080-08bb648d30c2" />

### 6.2 Validación de Entrada (HTTP 400 Bad Request)
<img width="1424" height="616" alt="Captura de pantalla 2026-09-21 181416" src="https://github.com/user-attachments/assets/d2753d11-b81e-4445-864f-00c391570684" />

### 6.3 Conflicto de Negocio por Duplicado (HTTP 409 Conflict)
<img width="1417" height="698" alt="Captura de pantalla 2026-09-21 181457" src="https://github.com/user-attachments/assets/469f2c1b-6d18-4233-b45f-1656cd24e0a0" />

---

## 7. Persistencia Políglota Elegida

- **PostgreSQL 16:** Entidades relacionales con integridad referencial y soporte ACID: catálogo de platos/tragos, usuarios, credenciales cifradas y comandas.
- **MongoDB 7:** Documentos semiestructurados de alta frecuencia: auditoría de eventos de carta (`eventos_restaurante`), logs transaccionales y modificaciones de recetas.

---

## 8. Seguridad (DevSecOps)

### 8.1 Matriz de Control de Acceso Basado en Roles (RBAC)

| Endpoint | Método HTTP | Roles Autorizados | Política de Acceso |
| :--- | :---: | :--- | :--- |
| `/api/v1/auth/login` | `POST` | *Público / Todos* | `permitAll()` |
| `/swagger-ui/**`, `/v3/api-docs/**` | `GET` | *Público / Todos* | `permitAll()` |
| `/api/v1/platos` | `GET` | *Público / Todos* | `permitAll()` |
| `/api/v1/platos/{id}` | `GET` | *Público / Todos* | `permitAll()` |
| `/api/v1/platos/disponibles` | `GET` | *Público / Todos* | `permitAll()` |
| `/api/v1/menu/**` | `GET` | *Público / Todos* | `permitAll()` |
| `/api/v1/platos` | `POST` | `ROLE_ADMIN`, `ROLE_CHEF` | `hasAnyRole('CHEF', 'ADMIN')` |
| `/api/v1/platos/{id}` | `PUT` | `ROLE_ADMIN`, `ROLE_CHEF` | `hasAnyRole('CHEF', 'ADMIN')` |
| `/api/v1/platos/{id}/disponible` | `PATCH` | `ROLE_ADMIN`, `ROLE_CHEF`, `ROLE_BARTENDER`, `ROLE_MESERO` | `hasAnyRole(...)` |
| `/api/v1/platos/{id}` | `DELETE` | `ROLE_ADMIN` | `hasRole('ADMIN')` |

### 8.2 Evidencias de Autenticación y Autorización en Swagger UI

#### Login Exitoso con JWT (200 OK)
<img width="1399" height="622" alt="Captura de pantalla 2026-10-08 163305" src="https://github.com/user-attachments/assets/d71f3c00-c6ff-4ac9-8c8a-6294ed0fb475" />


#### Configuración de Autenticación ("Authorize") en Swagger UI
<img width="645" height="278" alt="Captura de pantalla 2026-10-08 163518" src="https://github.com/user-attachments/assets/d287b288-321e-4e66-94e0-b3daa0e2160f" />


#### Invocación sin Token (401 Unauthorized)
<img width="1398" height="557" alt="Captura de pantalla 2026-10-08 163628" src="https://github.com/user-attachments/assets/4bfd0000-49bc-4e5f-83e1-2768df9296eb" />


#### Invocación con Rol Incorrecto (403 Forbidden)
<img width="1397" height="638" alt="Captura de pantalla 2026-10-08 164016" src="https://github.com/user-attachments/assets/03d72e4a-a218-4f20-aeb5-58ae0e56c447" />


### 8.3 Cabeceras de Seguridad Perimetral OWASP (Postman)
<img width="1084" height="503" alt="Captura de pantalla 2026-10-08 164707" src="https://github.com/user-attachments/assets/dd41dab7-d88e-4b8e-8b1c-be6fa733ad65" />


### 8.4 Checklist de Mitigación OWASP Top 10

| Vulnerabilidad OWASP | Estado | Mecanismo de Mitigación Aplicado |
| :--- | :---: | :--- |
| **A01: Broken Access Control** | ✅ Mitigado | Control RBAC granular a nivel de URL y método (`@PreAuthorize`), arquitectura Stateless por token JWT. |
| **A02: Cryptographic Failures** | ✅ Mitigado | Contraseñas protegidas con BCrypt (`BCryptPasswordEncoder`). Firma de tokens con HMAC-SHA256 (claves $\ge 256$ bits). |
| **A03: Injection (SQL/NoSQL)** | ✅ Mitigado | Consultas tipadas y parametrizadas vía Spring Data JPA (Hibernate) y Spring Data MongoDB. |
| **A05: Security Misconfiguration** | ✅ Mitigado | Cabeceras de seguridad activas (CSP, Frame-Options, XSS). Excepciones capturadas centralmente en `GlobalExceptionHandler` sin filtrar stack traces. |
| **A07: Identification and Authentication Failures** | ✅ Mitigado | JWT con expiración estricta (1 hora), invalidación perimetral de credenciales y validación de claims. |

### 8.5 Pruebas Automatizadas de Seguridad (`mvn test`)
<img width="1036" height="300" alt="Captura de pantalla 2026-10-08 165039" src="https://github.com/user-attachments/assets/37efbf30-a758-4a3b-8ddc-fce9fedd794c" />

---

## 9. Contenerización con Docker

### 9.1 Instrucciones para Ejecución Local
Para levantar el ecosistema completo (API + PostgreSQL 16 + MongoDB 7) en segundo plano:

```bash
# Construir imágenes y levantar contenedores
docker compose up --build -d

# Validar estado de salud de los servicios
docker compose ps

# Visualizar logs en tiempo real de la API
docker compose logs -f api
```

### 9.2 Verificación de Contenedores y Salud del Stack

#### Estado de Servicios (`docker compose ps`)
<img width="1855" height="169" alt="Captura de pantalla 2026-10-08 165812" src="https://github.com/user-attachments/assets/42d2bd3e-80be-41a4-b6b4-6ee675529aac" />


#### Swagger UI Funcionando en Contenedor Local
<img width="972" height="314" alt="Captura de pantalla 2026-10-08 170153" src="https://github.com/user-attachments/assets/469fcf80-b65d-4f97-81da-2dd49bc3fed3" />


#### Logs de Arranque de la API y Conexión a Base de Datos
<img width="1849" height="107" alt="Captura de pantalla 2026-10-08 170546" src="https://github.com/user-attachments/assets/eba1c333-522a-43e7-bb48-e84448776fa5" />


### 9.3 Imagen Pública en Docker Hub
<img width="1897" height="850" alt="Captura de pantalla 2026-10-08 170625" src="https://github.com/user-attachments/assets/860219a9-fdf4-439c-bf89-4eec2a44bc9e" />


### 9.4 Variables de Entorno del Contenedor

| Variable | Descripción | Valor por Defecto |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Perfil de configuración de Spring Boot. | `docker` |
| `SERVER_PORT` | Puerto de escucha HTTP del servidor embebido. | `8080` |
| `DB_HOST` | Host o nombre de servicio del contenedor PostgreSQL. | `postgres` |
| `DB_PORT` | Puerto de escucha de PostgreSQL. | `5432` |
| `DB_NAME` | Nombre de la base de datos relacional. | `restaurante` |
| `DB_USER` | Usuario de conexión a PostgreSQL. | `postgres` |
| `DB_PASSWORD` | Contraseña del usuario de PostgreSQL. | *(Secreto)* |
| `MONGO_HOST` | Host o nombre de servicio del contenedor MongoDB. | `mongo` |
| `MONGO_PORT` | Puerto de escucha de MongoDB. | `27017` |
| `MONGO_DB` | Base de datos documental para auditoría. | `restaurante` |
| `MONGO_USER` | Usuario administrador de MongoDB. | `admin` |
| `MONGO_PASSWORD` | Contraseña administradora de MongoDB. | *(Secreto)* |
| `JWT_SECRET` | Clave criptográfica HMAC-SHA256 para firma de tokens. | *(Secreto)* |
| `JWT_EXPIRATION` | Tiempo de vida del JWT en milisegundos (1 hora = 3600000). | `3600000` |

---

## 10. CI/CD y Despliegue Cloud (GitHub Actions & Azure)

### 10.1 URLs de los Ambientes en la Nube
- **Ambiente QA:** https://bluevelvet-api-qa-jmunar-fdfpf4c2azaugtce.canadacentral-01.azurewebsites.net/swagger-ui/index.html
- **Ambiente PROD:**https://bluevelvet-api-prod-jmunar-cuazejb0asckfwbb.canadacentral-01.azurewebsites.net/swagger-ui/index.html
### 10.2 Estrategia de Ambientes y Flujo de Despliegue

| Ambiente | Disparador | Estrategia de Despliegue | Tag de Imagen Docker |
| :--- | :--- | :--- | :--- |
| **QA** | `push` a `main` o `develop`, y `pull_request` a `main`/`develop`. | Despliegue continuo **automático** tras ejecutar pruebas unitarias/integración en service containers. | `qa-latest`, `qa-<sha>` |
| **PROD** | `push` de tags semánticos (`v*.*.*`). | Despliegue controlado con **aprobación manual obligatoria** (*Required Reviewers*) vía GitHub Environment `production`. | `vX.Y.Z`, `latest` |

### 10.3 Diagrama de Despliegue en la Nube
<img width="1279" height="932" alt="Despliegue drawio" src="https://github.com/user-attachments/assets/685bdd43-406d-4636-abf3-23bd78d1f4d4" />

### 10.4 Evidencias de Ejecución de Pipelines

#### Pipeline de CI/CD QA en Verde
<img width="1321" height="558" alt="Captura de pantalla 2026-10-08 170656" src="https://github.com/user-attachments/assets/33446a77-98da-4926-8aa4-c7e033a956a6" />

#### Puerta de Aprobación Manual en Producción
<img width="1899" height="700" alt="Captura de pantalla 2026-10-08 170857" src="https://github.com/user-attachments/assets/dde18621-e2fc-497c-b8a9-87ce5c0a412e" />

#### Versionamiento de Imágenes en Docker Hub
<img width="1584" height="899" alt="Captura de pantalla 2026-10-08 170925" src="https://github.com/user-attachments/assets/da3666e9-4a12-401c-9b80-82d51600d52a" />

#### Azure App Service Ejecutando el Contenedor
<img width="1884" height="878" alt="Captura de pantalla 2026-10-08 171037" src="https://github.com/user-attachments/assets/75a358c4-dc89-45a3-abc9-7931697527a9" />

### 10.5 Secretos Configurados en GitHub Actions
Configurados en **Settings** $\rightarrow$ **Secrets and variables** $\rightarrow$ **Actions**:

- `DOCKERHUB_USERNAME`: Usuario de Docker Hub.
- `DOCKERHUB_TOKEN`: Personal Access Token de Docker Hub con permisos de escritura.
- `AZURE_CREDENTIALS`: JSON del Service Principal para autenticación en Azure CLI.
- `AZURE_WEBAPP_NAME_QA`: Nombre del App Service para QA.
- `AZURE_WEBAPP_NAME_PROD`: Nombre del App Service para Producción.
- `JWT_SECRET_QA`: Clave criptográfica para tokens en QA.
- `JWT_SECRET_PROD`: Clave criptográfica independiente para tokens en Producción.
- `DB_HOST_QA`: Host de PostgreSQL Flexible Server en Azure (QA).
- `DB_HOST_PROD`: Host de PostgreSQL Flexible Server en Azure (PROD).
- `DB_PASSWORD_QA`: Contraseña de base de datos en QA.
- `DB_PASSWORD_PROD`: Contraseña de base de datos en Producción.
