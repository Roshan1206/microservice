# Microservice

## Commands
- Push images to docker hub
``` declarative
#docker image push docker.io/username/accounts:v1
docker image push docker.io/username/${modulename}
```

- Build docker images
  In the root folder run
``` declarative
mvn compile jib:dockerBuild
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

- Installing rabbitmq in docker
``` declarative
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.1.2-management
```