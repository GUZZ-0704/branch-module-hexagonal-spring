# Sucursal API — Spring Boot (Hexagonal, JWT, PostgreSQL)

Sistema para gestión de sucursales y su ecosistema: horarios, teléfonos (corporativos y de contacto), imágenes, empleados y asignaciones; con autenticación JWT, autorización por roles (ADMIN/EMPLOYEE) y subida de imágenes a filesystem.

---

## Características

* **Sucursales (Branches)**: CRUD, activar/desactivar, slug único.
* **Horarios (Schedules)**: CRUD por sucursal (por día), soporte `closed`.
* **Teléfonos**:

  * **BranchPhone** (corporativos y de contacto), prioridades, WhatsApp, publicación.
  * **EmployeePhone** (N teléfonos por empleado).
* **Empleados (Employee)**: CRUD, datos básicos, email institucional.
* **Asignaciones**:

  * **EmployeeBranchAssignment**: empleado ↔ sucursal (vigentes/activas).
  * **EmployeeCorporatePhoneAssignment**: teléfono corporativo asignado a empleado (no se “lleva” al cambiar de sucursal).
* **Imágenes de sucursal**: subida a filesystem (no en la base de datos), URL pública, “cover”.
* **Auth**: Login/Refresh con JWT; roles `ADMIN` y `EMPLOYEE`.
* **Autorización**: reglas por método HTTP y ruta (mutaciones solo ADMIN por defecto).
* **Arquitectura hexagonal por features**: domain / dto / port (in|out) / application (service, mapper) / adapter (in|out).

---

## Arquitectura (hexagonal por features)

```
com.example.sucursal_api
├─ branch/            # Branch (sucursal)
├─ schedule/          # Schedule (horarios)
├─ phone/             # BranchPhone
├─ image/             # BranchImage
├─ employee/          # Employee + EmployeePhone
├─ assignment/        # Assignments (empleado↔sucursal, tel corporativo)
├─ auth/              # Auth (login, jwt, useraccount)
├─ common/            # Soporte (validators, provider)
├─ file/              # Almacenamiento filesystem (StorageService)
└─ config/            # Seguridad, CORS, estáticos, seeders
```

Cada feature sigue el patrón:

```
feature/
  domain/        # entidades de dominio (POJO)
  dto/           # request/response records
  port/in|out/   # interfaces (casos de uso / repos)
  application/   # services/mappers (MapStruct)
  adapter/in/    # controllers REST
  adapter/out/   # JPA entities, repos impl
```

---

## Stack

* Java 21, Spring Boot 3.x
* Spring Web, Spring Security, Spring Data JPA
* PostgreSQL
* MapStruct
* JJWT (io.jsonwebtoken)
* Subidas: `multipart/form-data` + filesystem
* Maven

---

## Requisitos

* Java JDK 21 (o 17+)
* Maven 3.9+
* PostgreSQL 14+
* Opcional: DBeaver, Postman

---

## Configuración (application.properties)

Crear o ajustar `src/main/resources/application.properties`:

```properties
spring.application.name=sucursal_api

# JWT (>= 256 bits)
security.jwt.secret=HoalComoTaiestotienequesermuylargoparapoderfuncionarosinonoquierelevantartengomuchahambreyanosequemasponerlovoyaprobarasisinodavuelvo

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/sucursales
spring.datasource.username=postgres
spring.datasource.password=root
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.open-in-view=false

# Uploads (filesystem)
app.storage.root=uploads
app.storage.public-prefix=/files/
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Seeder Admin (activar solo una vez)
app.seed.admin=true
app.seed.admin.email=admin@empresa.com
app.seed.admin.password=Admin123!
app.seed.admin.firstName=Admin
app.seed.admin.lastName=Root

# (Opcional) logs de seguridad
# logging.level.org.springframework.security=DEBUG
```

En producción, usar `app.storage.root` con ruta absoluta (por ejemplo, `/var/app/uploads`) y configurar permisos adecuados.

---

## Carpeta de uploads

Crear la carpeta de almacenamiento fuera del classpath, en la raíz del proyecto (al nivel de `pom.xml`):

```
/sucursal_api
├─ pom.xml
├─ src/
└─ uploads/    <-- aquí
```

`.gitignore`:

```
/uploads/
```

El proyecto expone archivos públicos desde `/files/**`.

---

## Seeder de usuario Admin

El seeder opcional se controla con `app.seed.admin=true`. Al primer arranque crea:

* `Employee` activo con email institucional.
* `UserAccount` (username = email, password BCrypt).
* Rol `ADMIN` (sin prefijo `ROLE_`).

Después de creado, desactivar: `app.seed.admin=false`.

---

## Ejecución

Compilar y ejecutar:

```bash
mvn clean package
mvn spring-boot:run
# o
java -jar target/sucursal_api-*.jar
```

Si todo es correcto, Tomcat inicia en `:8080` y Hibernate crea/actualiza tablas (`ddl-auto=update`).

---

## Seguridad y roles

Reglas principales en el `SecurityConfig`:

* `POST/PUT/PATCH/DELETE /api/**` → `hasRole('ADMIN')` por defecto.
* `GET /api/branches/**` → `hasAnyRole('ADMIN','EMPLOYEE')`.
* `GET /api/**` → autenticado.
* Público: `/api/auth/**`, `OPTIONS /**`, `GET /files/**` (si se habilita).

Roles en la base de datos: guardar `ADMIN` y `EMPLOYEE` sin prefijo `ROLE_`. El filtro JWT añade `ROLE_` al construir `GrantedAuthority`.

---

## API Quickstart

### 1) Login

```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin@empresa.com",
  "password": "Admin123!"
}
```

Respuesta: `{ "accessToken": "...", "expiresIn": ... }`

### 2) Crear sucursal

```
POST /api/branches
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Sucursal Centro",
  "slug": "sucursal-centro",
  "address": "Av. Siempre Viva 123",
  "lat": -16.5,
  "lng": -68.15,
  "active": true
}
```

### 3) Subir imagen (multipart)

```
POST /api/branches/{branchId}/images
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <archivo-imagen>
title: Fachada
altText: Fachada principal
cover: true
```

La respuesta incluye `url` pública: `/files/branch/<uuid>/yyyy/MM/<uuid>.<ext>`.

### 4) Ver imagen

```
GET http://localhost:8080/files/branch/<uuid>/yyyy/MM/<uuid>.<ext>
```

### 5) Borrar imagen (archivo + fila)

```
DELETE /api/branches/{branchId}/images/{imageId}
Authorization: Bearer <token>
```

Los demás features (schedules, phones, employees, assignments) siguen rutas análogas bajo `/api/...` con validaciones de pertenencia y reglas de negocio.

---

## Dependencias clave (pom)

* `spring-boot-starter-web`
* `spring-boot-starter-security`
* `spring-boot-starter-data-jpa`
* `org.postgresql:postgresql`
* `io.jsonwebtoken:jjwt-api`, `jjwt-impl`, `jjwt-jackson`
* `org.mapstruct:mapstruct` y `mapstruct-processor` (annotationProcessor)
* `commons-io` (manejo de nombres de archivo)
* Opcional: `org.apache.tika:tika-core` para validar “magic numbers”

---

## Problemas comunes

* WeakKeyException (JWT): el secreto es corto o inválido. Usar clave de al menos 256 bits. Si se usa Base64, decodificar antes de `Keys.hmacShaKeyFor`.
* "Can't configure mvcMatchers after anyRequest": en `SecurityConfig` colocar todos los `requestMatchers(...)` antes de `.anyRequest().authenticated()`.
* 403 Forbidden en mutaciones: revisar rol del usuario. En base de datos debe ser `ADMIN` (sin `ROLE_`). Reloguear para regenerar token con roles actualizados.
* Campos `lat/lng` nulos: el DTO espera `lat/lng`; alinear nombres o usar `@JsonAlias`.
* Enum en schedule: si el cliente envía `"MONDAY"`, el DTO debe usar `enum Weekday` y la entidad `@Enumerated(EnumType.STRING)`.
* IDs nulos en respuestas (`branchId`, `employeeId`): en MapStruct mapear explícito (`@Mapping(target="branchId", source="branch.id")`) y en repos establecer relaciones con `EntityManager.getReference(...)`.
* Uploads no accesibles: crear `uploads/` en raíz, configurar `ResourceHandler` para `/files/**`, verificar permisos.

---

## Postman

Recomendado definir variables: `baseUrl`, `adminUsername`, `adminPassword`, `token`, `branchId`, `imageId`.
Flujo sugerido: Login → Crear sucursal → Subir imagen → Listar → Ver/Borrar.

---

## Licencia

MIT (o la que se defina para el proyecto).

---

## Notas

* Este README resume la puesta en marcha y decisiones de diseño (hexagonal, seguridad, filesystem).
* Para despliegues, considerar almacenamiento de archivos en S3/MinIO y configuración de backups.
* Se pueden proporcionar una colección Postman y/o `docker-compose` con PostgreSQL a solicitud.
