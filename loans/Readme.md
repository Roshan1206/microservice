# Loans-microservice

### Plugins

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
	<configuration>
        <image>
            <name>username/${project.artifactId}:s4</name>
        </image>
	</configuration>
</plugin>
```

### Running Loans-microservice

``` declarative
In local:
    using cmd:
        # build jar using maven
        # execute jar
        - mvn clean install
        - java -jar loans-0.0.1-SNAPSHOT.jar
    
    using ide: run main method

In docker:
    # build jar & image using maven
    - mvn clean install
    - mvn spring-boot:bild-image
    # build and run docker image
    - docker run -d -p 8090:8090 username/loans:version
```