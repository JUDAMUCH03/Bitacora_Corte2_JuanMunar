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

<img width="757" height="827" alt="image" src="https://github.com/user-attachments/assets/02dc51ae-738a-42dc-b76b-eb4f190d2239" />

### 4.2 Diagrama de Componentes General
Vista macro de los componentes del sistema y su organización por capas, evidenciando el flujo de dependencias hacia el dominio.

<img width="780" height="946" alt="image" src="https://github.com/user-attachments/assets/af972482-e60a-4310-89bf-2e2abd65ce58" />

### 4.3 Diagrama de Componentes Específicos
Detalle de los componentes del **módulo de órdenes y comandas**, que concentra las reglas de negocio críticas.

<img width="838" height="844" alt="image" src="https://github.com/user-attachments/assets/b6013214-f1f2-4163-a891-9ba3c9ed5658" />

### 4.4 Diagrama de Secuencia
Flujo completo de **creación de una orden, personalización de un ítem con destilado, mutación del destilado y avance del estado en el KDS**.

<img width="1677" height="940" alt="image" src="https://github.com/user-attachments/assets/e9e0ed7b-623c-49b2-ae08-d9f0001c2c3f" />

### 4.5 Diagrama de Contexto C4
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
> *Request de autenticación contra `/api/v1/auth/login` y retorno de token Bearer:*
<!-- CAPTURA: Login exitoso en Swagger con request y respuesta JWT -->
![Login Exitoso Swagger](docs/images/sec-01-login-jwt.png)

#### Configuración de Autenticación ("Authorize") en Swagger UI
> *Modal de autorización con el Bearer Token inyectado:*
<!-- CAPTURA: Botón Authorize en Swagger UI con el token ingresado -->
![Authorize Swagger UI](docs/images/sec-02-swagger-authorize.png)

#### Invocación sin Token (401 Unauthorized)
> *Petición a endpoint protegido sin cabecera Authorization:*
<!-- CAPTURA: Retorno 401 Unauthorized sin token -->
![401 Unauthorized](docs/images/sec-03-unauthorized-401.png)

#### Invocación con Rol Incorrecto (403 Forbidden)
> *Intento de creación con rol sin privilegios (ej. `ROLE_CLIENTE` invocando POST `/api/v1/platos`):*
<!-- CAPTURA: Retorno 403 Forbidden por RBAC -->
![403 Forbidden](docs/images/sec-04-forbidden-403.png)

### 8.3 Cabeceras de Seguridad Perimetral OWASP (Postman)
> *Respuesta HTTP evidenciando cabeceras de protección activa:*  
> `X-Frame-Options: DENY`, `Content-Security-Policy: default-src 'self'`, `X-Content-Type-Options: nosniff`, `X-XSS-Protection: 1; mode=block`.
<!-- CAPTURA: Cabeceras de seguridad en Postman -->
![Headers OWASP Postman](docs/images/sec-05-headers-owasp.png)

### 8.4 Checklist de Mitigación OWASP Top 10

| Vulnerabilidad OWASP | Estado | Mecanismo de Mitigación Aplicado |
| :--- | :---: | :--- |
| **A01: Broken Access Control** | ✅ Mitigado | Control RBAC granular a nivel de URL y método (`@PreAuthorize`), arquitectura Stateless por token JWT. |
| **A02: Cryptographic Failures** | ✅ Mitigado | Contraseñas protegidas con BCrypt (`BCryptPasswordEncoder`). Firma de tokens con HMAC-SHA256 (claves $\ge 256$ bits). |
| **A03: Injection (SQL/NoSQL)** | ✅ Mitigado | Consultas tipadas y parametrizadas vía Spring Data JPA (Hibernate) y Spring Data MongoDB. |
| **A05: Security Misconfiguration** | ✅ Mitigado | Cabeceras de seguridad activas (CSP, Frame-Options, XSS). Excepciones capturadas centralmente en `GlobalExceptionHandler` sin filtrar stack traces. |
| **A07: Identification and Authentication Failures** | ✅ Mitigado | JWT con expiración estricta (1 hora), invalidación perimetral de credenciales y validación de claims. |

### 8.5 Pruebas Automatizadas de Seguridad (`mvn test`)
> *Ejecución de la batería completa de pruebas unitarias y de seguridad en verde:*
<!-- CAPTURA: Resultado de mvn test con pruebas de seguridad en verde -->
![Pruebas Seguridad mvn test](docs/images/sec-06-mvn-test-green.png)

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
> *Servicios `restaurante-api`, `restaurante-postgres` (healthy) y `restaurante-mongo` en ejecución:*
<!-- CAPTURA: docker compose ps con servicios healthy -->
![Docker Compose PS Healthy](docs/images/docker-01-compose-ps.png)

#### Swagger UI Funcionando en Contenedor Local
> *Acceso disponible en `http://localhost:8080/swagger-ui/index.html`:*
<!-- CAPTURA: Swagger corriendo en contenedor local -->
![Swagger Local Docker](docs/images/docker-02-swagger-local.png)

#### Logs de Arranque de la API y Conexión a Base de Datos
> *Salida de logs del contenedor confirmando conexión a PostgreSQL y MongoDB:*
<!-- CAPTURA: Logs de la API en contenedor mostrando arranque exitoso -->
![Logs Docker API](docs/images/docker-03-api-logs.png)

### 9.3 Imagen Pública en Docker Hub
- **Repositorio Oficial:** [`${DOCKERHUB_USERNAME}/restaurante-api`](https://hub.docker.com/)

> *Vista del repositorio en Docker Hub con tags publicados:*
<!-- CAPTURA: Página de Docker Hub con la imagen -->
![Docker Hub Repository](docs/images/docker-04-dockerhub.png)

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
- **Ambiente QA:** [https://restaurante-qa.azurewebsites.net/swagger-ui/index.html](https://restaurante-qa.azurewebsites.net/swagger-ui/index.html)
- **Ambiente PROD:** [https://restaurante-prod.azurewebsites.net/swagger-ui/index.html](https://restaurante-prod.azurewebsites.net/swagger-ui/index.html)

### 10.2 Estrategia de Ambientes y Flujo de Despliegue

| Ambiente | Disparador | Estrategia de Despliegue | Tag de Imagen Docker |
| :--- | :--- | :--- | :--- |
| **QA** | `push` a `main` o `develop`, y `pull_request` a `main`/`develop`. | Despliegue continuo **automático** tras ejecutar pruebas unitarias/integración en service containers. | `qa-latest`, `qa-<sha>` |
| **PROD** | `push` de tags semánticos (`v*.*.*`). | Despliegue controlado con **aprobación manual obligatoria** (*Required Reviewers*) vía GitHub Environment `production`. | `vX.Y.Z`, `latest` |

### 10.3 Diagrama de Despliegue en la Nube
<!-- CAPTURA: Diagrama de despliegue en alta resolución -->
![Diagrama de Despliegue](docs/images/cicd-00-deployment-diagram.png)

### 10.4 Evidencias de Ejecución de Pipelines

#### Pipeline de CI/CD QA en Verde
> *Ejecución exitosa de jobs `test`, `build-and-push` y `deploy-qa`:*
<!-- CAPTURA: Pipeline GitHub Actions en verde -->
![Pipeline QA Verde](docs/images/cicd-01-pipeline-green.png)

#### Puerta de Aprobación Manual en Producción
> *Estado "Waiting for review" en el Environment `production` de GitHub Actions:*
<!-- CAPTURA: Aprobación manual del deploy a PROD -->
![Aprobación Manual PROD](docs/images/cicd-02-manual-approval.png)

#### Versionamiento de Imágenes en Docker Hub
> *Tags semánticos generados automáticamente en el registry:*
<!-- CAPTURA: Imagen en Docker Hub con tags de versión -->
![Tags Docker Hub](docs/images/cicd-03-dockerhub-tags.png)

#### Azure App Service Ejecutando el Contenedor
> *Panel de Azure Portal mostrando el estado activo del contenedor en App Service:*
<!-- CAPTURA: App Service en Azure con contenedor corriendo -->
![Azure App Service Contenedor](docs/images/cicd-04-azure-appservice.png)

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
