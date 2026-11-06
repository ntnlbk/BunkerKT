# Bunker
## Description

## Docker
- Start all containers and seed data:
```
docker compose up --build
```

- Stop and delete all containiers and volumes:
```
docker compose down -v
```

- Clear cache
```
docker compose build --no-cache
```

## PostgreSQL 

- CLI in/out:
```
docker exec -it bunker_postgres psql -U root -d bunker_db

\q
```

- All tables:
```
\dt
```

- Select all from table:
```
SELECT * FROM hobby;
```

## Cloudflare
- Create a public tunnel to port 8000
```
cloudflared tunnel --url http://localhost:8000
```



