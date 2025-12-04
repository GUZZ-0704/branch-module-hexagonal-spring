# ============================================
# STAGE 1: Build
# ============================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copiar pom.xml primero para cachear dependencias
COPY pom.xml .

# Descargar dependencias (cache layer)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir el JAR
RUN mvn clean package -DskipTests

# ============================================
# STAGE 2: Runtime
# ============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar JAR desde el builder
COPY --from=builder /app/target/*.jar app.jar

# Cambiar propietario
RUN chown -R appuser:appgroup /app

# Cambiar a usuario no-root
USER appuser

# Puerto por defecto
EXPOSE 8080

# Variables de entorno
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
