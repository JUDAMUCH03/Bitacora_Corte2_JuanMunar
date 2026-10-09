# ==========================================
# Etapa 1: Builder (Compilación y dependencias)
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Cachear dependencias de Maven
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copiar código fuente y empaquetar aplicación
COPY src ./src
RUN mvn clean package -DskipTests -q

# ==========================================
# Etapa 2: Runtime ligero de producción
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar artefacto compilado desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
