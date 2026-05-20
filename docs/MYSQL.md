# MySQL - Libro de Clases

Infraestructura compartida del stack. El `docker-compose.yml` vive en la raiz de este repositorio (`bff-libroclases`).

## Arrancar MySQL (Docker)

Desde la raiz de **bff-libroclases**:

```powershell
docker compose up -d
```

Verificar:

```powershell
docker compose ps
```

## Bases de datos

| Servicio | Base MySQL |
|----------|------------|
| ms-academico | `libroclases_academico` |
| ms-asistencia | `libroclases_asistencia` |
| bff-libroclases | `libroclases_auth` |

Usuario: `libroclases` / `clave123`  
Root (solo admin): `root` / `root`

## Orden completo de arranque

1. **MySQL** — `docker compose up -d` (en este repo)
2. **eureka-server** — puerto 8761
3. **ms-academico** — puerto 8081
4. **ms-asistencia** — puerto 8082
5. **bff-libroclases** — puerto 8083
6. **frontend-libroclases** — puerto 5173

## Herramientas GUI

- **DBeaver Community**: conexion a `localhost:3306` con usuario `libroclases`.

## Tests (Maven)

Los tests de cada microservicio usan **H2 en memoria** (perfil `test`), no requieren MySQL.

```powershell
.\mvnw.cmd test
```
