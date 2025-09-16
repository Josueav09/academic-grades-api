# API de Gestión de Calificaciones Académicas

Una API REST desarrollada con Spring Boot para la gestión de calificaciones académicas, que permite a profesores registrar notas y a estudiantes consultar sus calificaciones.

## 🚀 Características Principales

- **Autenticación JWT**: Sistema seguro de autenticación con tokens JWT
- **Control de roles**: Diferenciación entre estudiantes (STUDENT) y profesores (TEACHER)
- **CRUD de calificaciones**: Operaciones completas para gestión de notas académicas
- **Seguridad por endpoints**: Autorización basada en roles para cada operación
- **Validación de datos**: Validaciones robustas en DTOs y entidades
- **Documentación API**: Integración con Swagger/OpenAPI
- **Testing completo**: Pruebas unitarias e integración

## 🛠️ Tecnologías Utilizadas

- **Java 21** - Versión LTS más reciente
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security 6.x** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **JJWT 0.12.3** - Manejo de tokens JWT
- **H2 Database** - Base de datos en memoria para desarrollo
- **Lombok 1.18.30** - Reducción de código boilerplate
- **SpringDoc OpenAPI 2.3.0** - Documentación automática de API
- **Maven 3.x** - Gestión de dependencias
- **JUnit 5 + Mockito 5.11.0** - Testing

## 📋 Prerrequisitos

- **Java 21** o superior
- **Maven 3.6+**
- Base de datos H2 (incluida) para desarrollo

## ⚙️ Instalación y Configuración

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/prueba-tecnica-api.git
cd prueba-tecnica-api
```

2. **Configurar aplicación**

Crear `application.properties` en `src/main/resources/`:
```properties
# Base de datos H2 (desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Configuración JWT
jwt.secret=mySecretKeyForJwtTokensAtLeast256BitsLong
jwt.expiration=86400000

# Configuración del servidor
server.port=8080

# Logging
logging.level.com.example.pruebaTecnica=DEBUG
```

Para testing crear `application-test.properties` en `src/test/resources/`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
jwt.secret=testSecretKeyForJwtTokensAtLeast256BitsLong
jwt.expiration=86400000
```

3. **Instalar dependencias**
```bash
mvn clean install
```

4. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 🔐 Autenticación y Autorización

### Roles del Sistema
- **STUDENT**: Puede consultar sus propias calificaciones
- **TEACHER**: Puede crear, actualizar, eliminar y consultar calificaciones

### Flujo de Autenticación
1. Registro de usuario en `/api/auth/register`
2. Login en `/api/auth/login` (recibe JWT token)
3. Incluir token en header: `Authorization: Bearer <token>`

## 📚 Endpoints de la API

### Autenticación

#### Registrar Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "estudiante1",
    "email": "estudiante1@example.com",
    "password": "password123",
    "role": "STUDENT"
}
```

#### Iniciar Sesión
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "estudiante1",
    "password": "password123"
}
```

**Respuesta:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "estudiante1",
    "email": "estudiante1@example.com",
    "role": "STUDENT"
}
```

### Calificaciones

#### Crear Calificación (Solo Profesores)
```http
POST /api/grades
Authorization: Bearer <token>
Content-Type: application/json

{
    "course": "Matemáticas",
    "score": 18.5,
    "comments": "Excelente trabajo en álgebra",
    "studentUsername": "estudiante1"
}
```

#### Obtener Calificaciones del Estudiante
```http
GET /api/grades
Authorization: Bearer <token>
```

#### Obtener Calificación por ID
```http
GET /api/grades/{id}
Authorization: Bearer <token>
```

#### Actualizar Calificación (Solo Profesores)
```http
PUT /api/grades/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
    "course": "Matemáticas",
    "score": 19.0,
    "comments": "Mejoró significativamente",
    "studentUsername": "estudiante1"
}
```

#### Eliminar Calificación (Solo Profesores)
```http
DELETE /api/grades/{id}
Authorization: Bearer <token>
```

## 🗄️ Modelo de Datos

### Usuario (User)
```java
{
    "id": 1,
    "username": "estudiante1",
    "email": "estudiante1@example.com",
    "role": "STUDENT",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
}
```

### Calificación (Grade)
```java
{
    "id": 1,
    "course": "Matemáticas",
    "score": 18.5,
    "comments": "Excelente trabajo",
    "studentUsername": "estudiante1",
    "createdAt": "2024-01-15T14:20:00",
    "updatedAt": "2024-01-15T14:20:00"
}
```

## 🧪 Testing

Ejecutar todas las pruebas:
```bash
mvn test
```

Ejecutar pruebas específicas:
```bash
# Pruebas unitarias
mvn test -Dtest=GradeServiceTest

# Pruebas de integración
mvn test -Dtest=*IntegrationTest
```

### Cobertura de Pruebas
- **Pruebas unitarias**: Servicios y lógica de negocio
- **Pruebas de integración**: Controladores y endpoints
- **Validaciones**: DTOs y entidades
- **Seguridad**: Autorización por roles

## 📖 Documentación de API

Una vez ejecutada la aplicación, accede a:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## 🔍 Arquitectura y Patrones de Diseño

### Arquitectura por Capas
- **Controller Layer**: Manejo de requests HTTP y responses
- **Service Layer**: Lógica de negocio y reglas de dominio  
- **Repository Layer**: Acceso a datos y persistencia
- **Security Layer**: Autenticación JWT y autorización por roles

### Patrones Implementados
- **MVC (Model-View-Controller)**: Separación de responsabilidades
- **DTO (Data Transfer Object)**: Transferencia segura de datos
- **Repository Pattern**: Abstracción del acceso a datos
- **Dependency Injection**: Inversión de control con Spring
- **JWT Stateless Authentication**: Autenticación sin sesiones

### Componentes Clave

#### JwtUtils
Clase utilitaria para manejo centralizado de tokens JWT:
```java
// Generación de tokens con roles
public String generateJwtToken(Authentication authentication)

// Validación de tokens
public boolean validateJwtToken(String authToken)

// Extracción de username y roles
public String getUserNameFromJwtToken(String token)
public List<String> getRolesFromJwtToken(String token)
```

#### SecurityConfig
Configuración de Spring Security con:
- Filtros JWT personalizados
- Configuración de endpoints públicos/protegidos
- Manejo de excepciones de seguridad
- Política de sesiones STATELESS

### Variables de Entorno

Crear archivo `.env` en la raíz del proyecto (opcional):
```bash
# Configuración JWT
JWT_SECRET=mySecretKeyForJwtTokensAtLeast256BitsLong
JWT_EXPIRATION=86400000

# Base de datos (para producción)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=academic_grades
DB_USERNAME=root
DB_PASSWORD=password

# Puerto del servidor
SERVER_PORT=8080
```

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/example/pruebaTecnica/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── JwtAuthFilter.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── GradeController.java
│   │   ├── dto/
│   │   │   ├── GradeDto.java
│   │   │   ├── JwtResponse.java
│   │   │   ├── UserLoginDto.java
│   │   │   └── UserRegistrationDto.java
│   │   ├── entity/
│   │   │   ├── Grade.java
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   ├── GradeRepository.java
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   ├── GradeService.java
│   │   │   └── UserService.java
│   │   ├── util/
│   │   │   └── JwtUtils.java
│   │   └── PruebaTecnicaApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/example/pruebaTecnica/
    │   ├── controller/
    │   │   ├── AuthControllerIntegrationTest.java
    │   │   └── GradeControllerIntegrationTest.java
    │   └── service/
    │       └── GradeServiceTest.java
    └── resources/
        └── application-test.properties
```

## 🚨 Validaciones y Restricciones

### Calificaciones
- **Curso**: Obligatorio, máximo 100 caracteres
- **Puntuación**: Entre 0 y 20 puntos
- **Comentarios**: Máximo 500 caracteres
- **Estudiante**: Username válido y existente

### Usuarios
- **Username**: 3-50 caracteres, único
- **Email**: Formato válido, único
- **Password**: Mínimo 6 caracteres
- **Role**: STUDENT o TEACHER

## 🔍 Casos de Uso

### Profesor
1. Registrarse con rol TEACHER
2. Iniciar sesión y obtener JWT
3. Crear calificaciones para estudiantes
4. Actualizar calificaciones existentes
5. Eliminar calificaciones si es necesario

### Estudiante
1. Registrarse con rol STUDENT
2. Iniciar sesión y obtener JWT
3. Consultar sus propias calificaciones
4. Ver detalles de una calificación específica

## ❗ Manejo de Errores

La API maneja los siguientes códigos de error:

- `200 OK` - Operación exitosa
- `201 Created` - Recurso creado exitosamente
- `400 Bad Request` - Error de validación
- `401 Unauthorized` - Token inválido o faltante
- `403 Forbidden` - Acceso denegado por rol
- `404 Not Found` - Recurso no encontrado
- `500 Internal Server Error` - Error del servidor

## 🤝 Contribución

1. Fork el proyecto
2. Crear rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request


**Proyecto**: Prueba Técnica - API de Gestión de Calificaciones Académicas  
**Versión**: 0.0.1-SNAPSHOT  
**Descripción**: Prueba técnica GTOP

Para más información sobre el proyecto o consultas técnicas, contactar al desarrollador responsable.

## 🔗 Enlaces Útiles

- [Spring Boot 3.5.5 Documentation](https://docs.spring.io/spring-boot/docs/3.5.5/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JJWT Documentation](https://github.com/jwtk/jjwt) - Librería JWT utilizada
- [SpringDoc OpenAPI](https://springdoc.org/) - Documentación automática
- [H2 Database Documentation](https://www.h2database.com/html/main.html)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (una vez ejecutada la app)
- **H2 Console**: `http://localhost:8080/h2-console` (para desarrollo)