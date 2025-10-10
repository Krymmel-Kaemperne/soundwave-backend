# Soundwave Backend

Dette er en Spring Boot applikation til billetreservationssystemet Soundwave. Applikationen indeholder REST API endpoints til håndtering af events, sale, områder, sæder og reservationer.

## Forudsætninger

- Docker og Docker Compose installeret på din maskine
- Git til at klone repository

## Kørsel med Docker Compose

### 1. Start alle services

```bash
docker compose up -d
```

Dette starter tre services:
- **MySQL database** (port 3306, kun tilgængelig på localhost)
- **Spring Boot backend** (port 8080)
- **Frontend** (port 5500) (http://127.0.0.1:5500)

### 2. Tjek status på services

```bash
docker compose ps
```

Alle services skulle vise status "healthy".

### 3. Se logs

```bash
# Se logs for alle services
docker compose logs -f

# Se logs for en specifik service
docker compose logs -f backend
```

### 4. Stop services

```bash
docker compose down
```

### 5. Genstart med frisk database

Hvis du vil rydde databasen og starte forfra:

```bash
docker compose down
docker volume rm soundwave-backend_mysql_data
docker compose up -d
```

## Eksempler på API kald

### Hent alle events
```bash
curl http://localhost:8080/events
```

### Hent specifikt event
```bash
curl http://localhost:8080/events/1
```

### Hent seat map for et event
```bash
curl http://localhost:8080/events/1/seats/map
```

### Hold sæder
```bash
curl -X POST http://localhost:8080/api/events/1/seats/hold \
  -H "Content-Type: application/json" \
  -d '{
    "seatIds": [1, 2, 3],
    "sessionId": "session-123"
  }'
```

## Frontend

Frontend applikationen er tilgængelig på `http://localhost:5500`

Frontend er en Vanilla JavaScript applikation, der bruger Nginx til at servere statiske filer. Den kommunikerer med backend API'en via REST endpoints.

## Database

MySQL database kører i Docker container og er kun tilgængelig på `localhost:3306` for sikkerhed.

Database connection details:
- Host: localhost
- Port: 3306
- Database: soundwave
- Username: sounduser
- Password: soundpass

## Initialisering af data

Ved første opstart bliver databasen automatisk initialiseret med test data, herunder:
- 2 sale (Koncert Arena og Konference Sal)
- 18 events (koncerter, comedy shows, foredrag etc.)
- Sædeområder og sæder for alle events

## Fejlfinding

### Tjek om containers kører
```bash
docker ps
```

### Tjek logs for fejl
```bash
docker compose logs backend
docker compose logs frontend
docker compose logs mysql
```

### Genstart services
```bash
docker compose restart
```

### Ryd og genstart
```bash
docker compose down -v
docker compose up -d
```

## Sikkerhed

- Database er kun tilgængelig på localhost (127.0.0.1)
- Alle services kører i et isoleret Docker netværk
- API endpoints har CORS konfiguration til at tillade requests fra frontend