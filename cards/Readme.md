# Cards-microservice

### Plugins

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.6</version>
    <configuration>
        <to>
            <image>username/${project.artifactId}:s4</image>
        </to>
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
    - mvn compile jib:dockerBuild
    # build and run docker image
    - docker run -d -p 8090:8090 username/loans:version
```