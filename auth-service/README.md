# Auth Service

## Descripcion

Microservicio encargado de la autenticación y registro de usuarios del sistema de monopatines eléctricos. Permite realizar login y registro de usuarios, generando tokens JWT para la autenticación en otros microservicios. Este servicio no utiliza base de datos propia, sino que se comunica con accounts-service para validar credenciales y crear usuarios.

## Dependencias

### spring-boot-starter-web
**Para que sirve:** Framework web de Spring Boot. Permite crear endpoints REST, manejar requests HTTP, y toda la funcionalidad web del servicio.

### spring-boot-starter-security
**Para que sirve:** Framework de seguridad de Spring. Permite configurar la seguridad del servicio. En este caso, todos los endpoints son públicos ya que el servicio solo genera tokens JWT.

### jjwt (io.jsonwebtoken)
**Para que sirve:** Librería para generar tokens JWT. Incluye `jjwt-api`, `jjwt-impl` y `jjwt-jackson` para crear tokens JWT que incluyen el userId y los roles del usuario.

### spring-boot-starter-validation
**Para que sirve:** Validación de datos de entrada. Permite validar requests (email válido, campos requeridos, etc.) usando anotaciones como `@Email`, `@NotNull`, `@NotBlank`.

### springdoc-openapi-starter-webmvc-ui
**Para que sirve:** Documentación automática de la API con Swagger/OpenAPI. Expone la documentación interactiva de los endpoints en `/swagger-ui/index.html` y el esquema OpenAPI en `/v3/api-docs`.

### spring-boot-starter-test
**Para que sirve:** Testing. Permite escribir y ejecutar tests unitarios e integración del servicio.

## Endpoints

### Autenticación (Authentication)

#### POST /api/auth/login
**Descripcion:** Autentica un usuario con email y password, y retorna un token JWT.
- **Rol requerido:** Público (no requiere autenticación)
- **Body:** `LoginRequest` (email, password)
- **Respuesta:** `LoginResponse` con token JWT, userId, email y roles (HTTP 200)
- **Errores:** HTTP 401 si las credenciales son inválidas, HTTP 404 si el usuario no existe
- **Nota:** Este endpoint valida las credenciales llamando a `POST /api/accounts/users/validate-password` en accounts-service, luego obtiene los datos del usuario con `GET /api/accounts/users?email={email}` y finalmente genera el token JWT.

#### POST /api/auth/register
**Descripcion:** Registra un nuevo usuario en el sistema.
- **Rol requerido:** Público (no requiere autenticación)
- **Body:** `RegisterRequest` (firstName, lastName, email, phoneNumber, password)
- **Respuesta:** `RegisterResponse` con userId, email y mensaje de éxito (HTTP 201)
- **Errores:** HTTP 409 si el email ya existe
- **Nota:** Este endpoint hashea el password automáticamente antes de enviarlo a accounts-service. El usuario creado recibe el rol `ROLE_USER` por defecto. Se comunica con `POST /api/accounts/users` en accounts-service para crear el usuario.

## Comunicación con Accounts Service

Este servicio se comunica con accounts-service mediante `RestTemplate` para:
- Validar credenciales de usuario (`POST /api/accounts/users/validate-password`)
- Obtener datos de usuario por email (`GET /api/accounts/users?email={email}`)
- Crear nuevos usuarios (`POST /api/accounts/users`)


