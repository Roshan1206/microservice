# Microservice

## Run Locally
- Run Configserver
- Run Eurekaserver
- Run Accounts
- Run Cards
- Run Loans

Access Eureka dashboard at http://localhost:8070 and apps details at http://localhost:8070/eureka/apps

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
- Installing mysql in docker
``` declarative
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.1.2-management
docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accontsdb -d mysql
docker run -p 3307:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb -d mysql
docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql
```