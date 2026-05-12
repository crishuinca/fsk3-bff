# bff-libroclases

Backend For Frontend de la Plataforma de Libro de Clases Digital del Colegio Bernardo O'Higgins.

Este servicio es la entrada principal del frontend hacia el backend. No tiene base de datos propia: consume `ms-academico` y `ms-asistencia`, combina sus respuestas y entrega datos más cómodos para la interfaz React.

## Tecnologías

- Java 17
- Spring Boot 3.5.7
- Maven
- Spring Web
- RestClient
- Resilience4j Circuit Breaker
- Actuator
- Bean Validation
- Swagger / OpenAPI con springdoc 2.8.6
- JUnit 5, Mockito y JaCoCo

## Puerto y URLs

El servicio corre en el puerto `8083`.

- API base: `http://localhost:8083/api/v1`
- Swagger UI: `http://localhost:8083/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`
- Health: `http://localhost:8083/actuator/health`
- Circuit Breakers: `http://localhost:8083/actuator/circuitbreakers`
- Eventos Circuit Breaker: `http://localhost:8083/actuator/circuitbreakerevents`

## Servicios que consume

El BFF necesita que estén levantados:

- `ms-academico`: `http://localhost:8081/api/v1`
- `ms-asistencia`: `http://localhost:8082/api/v1`

Configuración principal:

```properties
ms-academico.url=http://localhost:8081/api/v1
ms-asistencia.url=http://localhost:8082/api/v1
```

## Cómo ejecutar

Primero levantar los microservicios:

```powershell
cd "C:\Users\tobal\Desktop\Fullstack 3\ms-academico"
.\mvnw.cmd spring-boot:run
```

```powershell
cd "C:\Users\tobal\Desktop\Fullstack 3\ms-asistencia"
.\mvnw.cmd spring-boot:run
```

Luego levantar el BFF:

```powershell
cd "C:\Users\tobal\Desktop\Fullstack 3\bff-libroclases"
.\mvnw.cmd spring-boot:run
```

Para compilar:

```powershell
.\mvnw.cmd clean compile
```

## Tests y cobertura

Ejecutar tests con reporte de cobertura:

```powershell
.\mvnw.cmd verify
```

El reporte HTML de JaCoCo queda en:

```text
target/site/jacoco/index.html
```

Estado actual:

- 21 tests.
- Cobertura global aproximada: 84% por líneas.
- Regla JaCoCo: mínimo 60% para la capa `service`.

## CI/CD y SonarQube

El repositorio incluye un pipeline de GitHub Actions en:

```text
.github/workflows/ci-sonar.yml
```

El pipeline ejecuta:

- Java 17.
- `.\mvnw.cmd verify` equivalente en Linux: `./mvnw -B verify`.
- Reporte JaCoCo como artefacto.
- Análisis SonarQube/SonarCloud si existen las variables y secretos necesarios.

Para activar Sonar en GitHub se debe configurar:

- Secret: `SONAR_TOKEN`
- Variable: `SONAR_ORGANIZATION`
- Variable opcional: `SONAR_PROJECT_KEY`
- Variable opcional: `SONAR_HOST_URL` si se usa SonarQube propio en vez de SonarCloud.

## Endpoints principales

Consultas compuestas:

- `GET /api/v1/anotacionDetalle/{id}`
- `GET /api/v1/asistenciaDetalle/{id}`
- `GET /api/v1/perfilEstudiante/{estudianteId}`
- `GET /api/v1/perfilEstudianteRut/{rut}`

Registros desde el frontend:

- `POST /api/v1/anotaciones`
- `POST /api/v1/asistencias`

## Responsabilidades del BFF

- Recibir las solicitudes del frontend.
- Consultar `ms-academico` para obtener estudiante y curso.
- Consultar `ms-asistencia` para obtener asistencias y anotaciones.
- Combinar datos de distintos microservicios en respuestas más completas.
- Evitar que el frontend tenga que conocer los endpoints internos de cada microservicio.
- Completar automáticamente el `cursoId` al registrar asistencia o anotación.

## Circuit Breaker

El proyecto usa Resilience4j para tolerar caídas temporales de los microservicios.

Circuit breakers configurados:

- `academico`
- `asistencia`

Demo sugerida:

1. Levantar `ms-academico`, `ms-asistencia` y `bff-libroclases`.
2. Probar un endpoint compuesto desde Swagger del BFF.
3. Apagar uno de los microservicios.
4. Repetir la consulta y revisar el fallback.
5. Consultar `http://localhost:8083/actuator/circuitbreakers`.

## Rol dentro de la arquitectura

El flujo esperado es:

```text
Frontend React -> BFF -> ms-academico
                    |
                    -> ms-asistencia
```

El frontend debe consumir solo el BFF. Esto simplifica la interfaz y centraliza la comunicación entre frontend y microservicios.
