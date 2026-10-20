# geolocalizador-backend

Backend completo para el ecosistema de geolocalización, control de flota y monitoreo de tiempos de espera (Standby).
Construido con **Spring Boot 3.3.4**, **Java 17/24**, **Spring Security (JWT multi-tenant)**, **WebSockets (STOMP)** y **MySQL Spatial**.

---

##  Arquitectura y Módulos Implementados

### 1. Cálculo de Ruteo, Distancia y Tiempo Estimado (ETA)
- **`RoutingService`**:
    - Consulta rutas vehiculares a través de **OSRM** (`https://router.project-osrm.org`) o servidor local/privado configurable vía `app.routing.osrm-url`.
    - **Mecanismo de Respaldo Resiliente (Haversine + factor de sinuosidad terrestre)**: Si la API externa no responde o no hay conexión a internet, calcula la distancia geodésica y el tiempo estimado promedio (45-50 km/h) automáticamente. Nunca bloquea ni tumba el sistema.
    - Guarda en [`Viaje`](file:///c:/Users/LENOVO/.gemini/antigravity/scratch/standby-backend/src/main/java/com/standby/backend/entity/Viaje.java): `distanciaEstimadaKm`, `duracionEstimadaMinutos`, `etaEstimado`, `ultimaLatitud`, `ultimaLongitud`, `ultimaUbicacionTimestamp`.
    - Endpoint de recálculo bajo demanda: `POST /api/driver/viaje-activo/recalcular-eta?latitud=...&longitud=...`.

### 2. Namespace de Administración (`/api/admin/**`)
Protegido para roles `ADMIN_TRANSPORTADORA` con aislamiento estricto multi-tenant por `empresaId`:
- **Viajes (`/api/admin/viajes`)**:
    - `POST /api/admin/viajes`: Despachar/crear viaje (valida vehículo y conductor libres, sitios origen/destino, calcula distancia y ETA inicial).
    - `POST /api/admin/viajes/{id}/iniciar-ruta`: Cambia de `CREADO` a `EN_RUTA`.
    - `GET /api/admin/viajes`: Listado con filtro opcional `?estado=...`.
    - `GET /api/admin/viajes/{id}`: Detalle completo del viaje con nombres de sitios, placas y conductor.
    - `POST /api/admin/viajes/{id}/cancelar`: Cancelación controlada de viaje.
- **Conductores (`/api/admin/conductores`)**:
    - `GET /api/admin/conductores`, `POST`, `PUT /{id}`, `DELETE /{id}` (desactivación lógica).
    - Hasheo automático de contraseñas con `BCryptPasswordEncoder`.
- **Vehículos (`/api/admin/vehiculos`)**:
    - `GET /api/admin/vehiculos`, `POST`, `PUT /{id}`, `DELETE /{id}` (asociación de conductor y validación de placa única).
- **Sitios y Geocercas (`/api/admin/sitios`)**:
    - CRUD de sitios con coordenadas, radio de geocerca en metros y SLA de permanencia en minutos.
- **Monitoreo de Standby (`/api/admin/standby`)**:
    - `GET /api/admin/standby`: Registros de espera en sitio (`minutosStandby`, si superó el SLA, cerrado o en curso).
    - `GET /api/admin/standby/resumen`: Métricas ejecutivas (total registros, activos, excedidos, promedio minutos).
- **Incidentes (`/api/admin/incidentes`)**:
    - `GET /api/admin/incidentes`: Listar incidentes reportados por conductores o generados por el sistema.
    - `PUT /api/admin/incidentes/{id}/resolver`: Marcar como resuelto con notas de cierre.

### 3. Namespace del Conductor (`/api/driver/**`)
Protegido para rol `CONDUCTOR`:
- `GET /api/driver/viaje-activo`: Datos completos del viaje activo, coordenadas del destino, distancia y ETA estimado.
- `POST /api/driver/ubicacion`: Envío periódico de GPS (evaluación de geocerca anti-rebote, creación automática de `RegistroStandby`, actualización de última posición y broadcast a WebSocket).
- `POST /api/driver/viaje/{id}/iniciar-atencion`: Cierra la ventana de standby (espera) y arranca servicio.
- `POST /api/driver/viaje/{id}/fin-atencion`: Finaliza el viaje.
- `POST /api/driver/viaje/{id}/incidentes`: Reporte de fallas en ruta.

### 4. WebSocket & STOMP en Tiempo Real
- Conexión: `/ws` (soporta SockJS y WebSocket nativo).
- Broker de mensajes: `/topic/empresas/{empresaId}/tracking`.
- Cada vez que un chofer envía su GPS a `/api/driver/ubicacion`, se emite un payload en tiempo real al mapa del panel web administrativo.

### 5. Jobs Programados (`@Scheduled`)
- **Alerta de Señal Perdida**: Revisa camiones en estado `EN_RUTA`. Si pasan más de `app.standby.timeout-sin-senal-minutos` (15 min) sin reportar coordenadas, crea un incidente de tipo `SISTEMA`.
- **Alerta de Exceso de Standby**: Revisa camiones en espera que superen el SLA propio del sitio o el umbral global (`app.standby.umbral-alerta-minutos`, 120 min) y genera alerta automática.

### 6. Autenticación y Refresh Token (`/api/auth/**`)
- `POST /api/auth/login`: Autentica credenciales y genera Access Token (8h) + Refresh Token (7 días).
- `POST /api/auth/refresh`: Renueva el token de acceso sin pedir contraseñas.

---

##  Cómo Ejecutar

```bash
# Variables de entorno opcionales (tienen valores por defecto locales)
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=standby_db
export DB_USER=root
export DB_PASSWORD=tu_password
export JWT_SECRET=una-clave-larga-y-aleatoria-de-al-menos-32-caracteres-para-produccion

# Compilar y ejecutar
./mvnw clean spring-boot:run
```
