# CRUD-Test

## Características principales

### 1. Registro y gestión de usuarios

- **Registro de usuario:** Permite crear usuarios nuevos utilizando un DTO de autenticación, asignando roles y planes por defecto.
- **Completar registro:** Los usuarios pueden completar su perfil posteriormente, pasando de estado `PENDING` a `ACTIVE`, utilizando un flujo de token parcial que expira en 15 minutos.
- **Manejo de estados:** Implementa validaciones para la transición de estados y desactivación de usuarios.

### 2. Autenticación y seguridad

- **Autenticación JWT:** Emite tokens JWT firmados, tanto completos como parciales (para procesos de registro incompletos).
- **Validación de token:** Verifica la validez y expiración de los tokens.
- **CORS configurado:** Permite solicitudes desde `http://localhost:3000` y métodos `GET`, `POST`, `PUT`, `DELETE`.

### 3. Manejo de tokens parciales

- **Flujo seguro para nuevos registros:** Los nuevos usuarios reciben un token parcial de corta duración que debe ser validado y consumido antes de activar su cuenta.
- **Prevención de uso múltiple:** Los tokens parciales son de un solo uso y se eliminan/invalidan si expiran o se usan.

### 4. Pruebas automatizadas

- **Cobertura de repositorios:** Se incluyen pruebas para el repositorio de tokens parciales, cubriendo casos de éxito y fallos, garantizando la integridad del flujo de activación.
- **Configuración de entorno de pruebas:** Uso de perfiles y bases de datos específicas para testing, asegurando entornos aislados.

### 5. Arquitectura y tecnologías

- **Spring Boot y Spring Data JPA**
- **MapStruct para mapeos entre DTOs y entidades**
- **JWT (JSON Web Token) para autenticación**
- **Módulo preparado para integración con AI (Spring AI)**
- **Transacciones y validaciones con anotaciones estándar de Spring**

## Estructura general del proyecto

- `module/auth/`: Servicios y utilidades para generación y validación de tokens.
- `module/user/`: Lógica de negocio para registro, cambio de estado, y gestión de usuarios.
- `infra/security/`: Configuración de seguridad y CORS.
- `test/`: Pruebas unitarias y de integración para los módulos críticos.

## Instalación y ejecución

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/YmidOrtega/CRUD-Test.git
   cd CRUD-Test
   ```
2. **Configura tus variables de entorno** (si decides externalizar claves)
3. **Ejecuta la aplicación**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Ejecuta pruebas**
   ```bash
   ./mvnw test
   ```

## Uso de la API

- **Registro de usuario:** `POST /users/register`
- **Completar registro:** `POST /users/complete-registration`
- **Obtener información usuario:** `GET /users/{id}`
- **Desactivar usuario:** `PUT /users/{id}/deactivate`
- **Autenticación y obtención de token:** `POST /auth/login`

## Consideraciones de seguridad

- La clave JWT actualmente está hardcodeada por simplicidad, pero se recomienda parametrizarla mediante variables de entorno para producción.
- Los tokens parciales y definitivos tienen tiempos de expiración controlados y validaciones exhaustivas.

## Futuras extensiones

- Integración completa con servicios de AI.
- Internacionalización y ampliación de CORS.
- Flujos avanzados de recuperación de contraseña y gestión de roles.

---

Desarrollado por [Ymid Ortega](https://github.com/YmidOrtega)
