# Spring Boot practice

A simple Spring Boot application using [HTMX](https://htmx.org/examples/)+[PicoCSS](https://picocss.com/docs/containers.html) and j2html for the frontend.

## Build

`mvn install`

## Run Tests

`mvn test`

## Run

First, start Postgres:

```bash
docker run --name logitdb -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_USER=logit -p 5432:5432 -d postgres
```

After build, run:

```bash
java -jar ./target/logit-0.0.1-SNAPSHOT.jar
```

Or

```bash
mvn spring-boot:run
```

## Inspiration

https://anthonybruno.dev/2023/06/11/full-stack-development-in-a-single-java-file/
https://j2html.com/examples.html

## Future?

 - Potentially look at Thymeleaf for templates. See f.ex [this](https://github.com/wiverson/htmx-demo?tab=readme-ov-file) 
 - Deploy somewhere
 - Add not hard coded users (see f.ex https://www.baeldung.com/spring-security-jdbc-authentication)
