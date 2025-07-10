# Accounts-microservice

### Running Accounts-microservice

``` declarative

In local:
    using cmd:
        # build jar using maven
        # execute jar
        - mvn clean install
        - java -jar accounts-0.0.1-SNAPSHOT.jar
    
    using ide: run main method

In docker:
    # build jar using maven
    - mvn clean install
    # build and run docker image
    - docker build -t username/accounts:version
    - docker run -d -p 8080:8080 username/accounts:version
```