# Microservice

## Commands
- Push images to docker hub
``` declarative
#docker image push docker.io/username/accounts:v1
docker image push docker.io/username/${modulename}
```
- start all services using docker
``` declarative
docker compose up -d
```

- Stop all services
``` declarative
docker compose stop
```

- Restart all services
``` declarative
docker compose start
```

- Stop and Remove all services
``` declarative
docker compose down
```