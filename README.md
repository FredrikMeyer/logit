# Spring Boot practice

[![Java CI with Maven](https://github.com/FredrikMeyer/logit/actions/workflows/maven.yml/badge.svg)](https://github.com/FredrikMeyer/logit/actions/workflows/maven.yml)

A simple Spring Boot application using [HTMX](https://htmx.org/examples/)+[PicoCSS](https://picocss.com/docs/containers.html) and j2html for the frontend.

## Build

`mvn install`

Or:

`mvn package -Dmaven.test.skip`

## Run Tests

`mvn test`

## Run

First, start Postgres:

```bash
docker run --name logitdb -e POSTGRES_PASSWORD=mysecretpassword -e POSTGRES_USER=logit -p 5432:5432 -d postgres
```

Then start Redis:
```bash
docker run --name my-redis -p 6379:6379 -d redis
```

After build, run:

```bash
java -jar ./target/logit-0.0.1-SNAPSHOT.jar
```

Or

```bash
mvn spring-boot:run
```

## Run locally in Kubernetes with Kind

First, create your cluster with

```bash
kind create cluster
```

Then build the application as a Docker image by running

```bash
docker build -t logit:v2 .
```

in the root directory. Transfer the image to the local cluster by:

```bash
kind load docker-image --name kind logit:v2
```

Then run

```
kubectl -n default apply -f redis.yaml
kubectl -n default apply -f postgres.yaml
kubectl -n default apply -f application.yaml  # Maybe wait before redis+postgres are ready
```

To be able to test locally, you have to port forward:

```bash
kubectl port-forward pod/$(kubectl get pods -l app=logit-app --no-headers | awk 'NR==1{print $1}') 8080:8080
```

When done, run

```bash
kind delete cluster
```

## Inspiration

https://anthonybruno.dev/2023/06/11/full-stack-development-in-a-single-java-file/
https://j2html.com/examples.html

## Future?

 - Potentially look at Thymeleaf for templates. See f.ex [this](https://github.com/wiverson/htmx-demo?tab=readme-ov-file) 
 - Deploy somewhere
 - Add not hard coded users (see f.ex https://www.baeldung.com/spring-security-jdbc-authentication)


### Plan for user db

Look at this one: https://www.bezkoder.com/spring-boot-security-postgresql-jwt-authentication/

Need to implement UserDetailsService og UserRepository.
