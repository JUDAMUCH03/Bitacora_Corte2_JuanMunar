# Guía de Configuración CI/CD — GitHub Actions y Azure App Service (DOSW S10)

Esta guía detalla el aprovisionamiento, configuración de secretos y gobernanza requerida para operar la infraestructura de Integración y Entrega Continua (CI/CD) del backend **Blue Velvet** en Azure App Service y Docker Hub.

---

## 1. Matriz de Secretos Requeridos en GitHub Actions

Para configurar estos secretos, navegue en el repositorio a:  
**Settings** $\rightarrow$ **Secrets and variables** $\rightarrow$ **Actions** $\rightarrow$ **New repository secret**.

| Nombre del Secreto | Descripción | Ámbito / Ejemplo |
| :--- | :--- | :--- |
| `DOCKERHUB_USERNAME` | Nombre de usuario de la cuenta u organización en Docker Hub. | Global (`ej. juanmunar`) |
| `DOCKERHUB_TOKEN` | Personal Access Token (PAT) con permisos **Read & Write** generado en Docker Hub. | Global (`dckr_pat_...`) |
| `AZURE_CREDENTIALS` | Objeto JSON con las credenciales del Service Principal para autenticar GitHub Actions contra Azure. | Global (JSON generado con Azure CLI) |
| `AZURE_WEBAPP_NAME_QA` | Nombre del recurso Azure App Service (Linux Web App) asignado al ambiente de QA. | QA (`app-bluevelvet-qa`) |
| `AZURE_WEBAPP_NAME_PROD` | Nombre del recurso Azure App Service (Linux Web App) asignado al ambiente de Producción. | PROD (`app-bluevelvet-prod`) |
| `JWT_SECRET_QA` | Clave secreta Base64 (mínimo 256 bits) para firma y verificación de tokens JWT en QA. | QA (`TXkgc3VwZXIgc2VjcmV0IGtleSBwYXJhIFFB...`) |
| `JWT_SECRET_PROD` | Clave secreta Base64 exclusiva para producción (estrictamente diferenciada de QA). | PROD (`TXkgcHJvZHVjdGlvbiBzdXBlciBzZWNyZXQ...`) |
| `DB_HOST_QA` | Nombre de host (FQDN) del servidor Azure Database for PostgreSQL Flexible Server en QA. | QA (`psql-bluevelvet-qa.postgres.database.azure.com`) |
| `DB_HOST_PROD` | Nombre de host (FQDN) del servidor Azure Database for PostgreSQL Flexible Server en Producción. | PROD (`psql-bluevelvet-prod.postgres.database.azure.com`) |
| `DB_PASSWORD_QA` | Contraseña del usuario administrador de base de datos en QA. | QA |
| `DB_PASSWORD_PROD` | Contraseña del usuario administrador de base de datos en Producción. | PROD |

> [!SECURITY]
> **Aislamiento Criptográfico:** Nunca comparta el valor de `JWT_SECRET_QA` con `JWT_SECRET_PROD`. La clave de producción debe ser generada con entropía criptográfica fuerte (ej. `openssl rand -base64 32`).

---

## 2. Aprovisionamiento del Service Principal en Azure CLI

El pipeline utiliza el Service Principal para autenticarse de forma no interactiva mediante la acción oficial `azure/login@v1`.

### Paso 1: Iniciar sesión en Azure CLI
```bash
az login
```

### Paso 2: Obtener el ID de la Suscripción activa
```bash
az account show --query id -o tsv
```

### Paso 3: Crear el Service Principal con Rol Contributor
Reemplace `{subscription-id}` y `{resource-group}` con los valores correspondientes a su infraestructura en Azure:

```bash
az ad sp create-for-rbac \
  --name "github-actions-restaurante" \
  --role contributor \
  --scopes /subscriptions/{subscription-id}/resourceGroups/{resource-group} \
  --json-auth
```

### Paso 4: Configurar el secreto en GitHub
El comando anterior retornará una salida en formato JSON con la siguiente estructura:

```json
{
  "clientId": "<GUID>",
  "clientSecret": "<SECRET_VALUE>",
  "subscriptionId": "<GUID>",
  "tenantId": "<GUID>",
  "activeDirectoryEndpointUrl": "https://login.microsoftonline.com",
  "resourceManagerEndpointUrl": "https://management.azure.com/",
  "activeDirectoryGraphResourceId": "https://graph.windows.net/",
  "sqlManagementEndpointUrl": "https://management.core.windows.net:8443/",
  "galleryEndpointUrl": "https://gallery.azure.com/",
  "managementEndpointUrl": "https://management.core.windows.net/"
}
```

Copie todo el contenido JSON (incluidas las llaves `{}`) y regístrelo íntegramente como el secreto `AZURE_CREDENTIALS` en GitHub Actions.

---

## 3. Configuración del GitHub Environment `production` (Puerta de Aprobación Manual)

Para asegurar la gobernanza y cumplir con la política de control de cambios antes de desplegar en producción:

1. Ingrese a la configuración del repositorio en GitHub: **Settings** $\rightarrow$ **Environments**.
2. Haga clic en **New environment** y asigne el nombre exacto: `production`.
3. En la sección **Deployment protection rules**, active la casilla **Required reviewers**.
4. Agregue a los usuarios o equipos autorizados (ej. Tech Lead, Release Manager, DevOps Lead) como revisores obligatorios.
5. *(Opcional)* Configure la rama/etiqueta de despliegue en **Deployment branches** seleccionando **Selected tags** y especificando el patrón `v*.*.*`.
6. Haga clic en **Save protection rules**.

> [!NOTE]
> Cuando se publique un tag semántico `v*.*.*`, el job `deploy-prod` entrará en estado *Waiting* hasta que uno de los revisores designados apruebe el despliegue desde la interfaz de GitHub Actions.

---

## 4. Topología y Comportamiento de los Pipelines

### Pipeline QA (`.github/workflows/ci-qa.yml`)
* **Disparadores:**
  * `push` en ramas `main` y `develop`.
  * `pull_request` con destino a `main`.
* **Fases:**
  1. **🧪 Test:** Configura Java 21 Temurin, restaura dependencias Maven de caché, corre `mvn test` y preserva el reporte JaCoCo (`target/site/jacoco/`).
  2. **🐳 Build & Push:** Construye la imagen multi-etapa y publica con tags `qa-latest` y `qa-<commit-sha>` en Docker Hub aprovechando caché GitHub Actions (`type=gha`).
  3. **🚀 Deploy QA:** Despliega en Azure Web App QA e inyecta dinámicamente las variables de configuración del perfil `docker`.

### Pipeline PROD (`.github/workflows/ci-prod.yml`)
* **Disparadores:**
  * `push` de tags semánticos (ej. `v1.0.0`, `v2.1.0`).
* **Fases:**
  1. **🐳 Build PROD:** Empaqueta y etiqueta con el tag de versión semántica (ej. `1.0.0`) y `latest`.
  2. **🚀 Deploy PROD:** Requiere aprobación manual vía Environment `production`. Despliega el tag inmutable en el Web App productivo y ajusta variables de entorno.
  3. **📣 Notificación:** Emite en los logs del pipeline el resumen y la URL de Swagger UI para validación operativa.
