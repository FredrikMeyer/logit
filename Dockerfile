FROM openjdk:21
LABEL authors="fredrikmeyer"

COPY ./target/logit-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "logit-0.0.1-SNAPSHOT.jar", "-Dspring.profiles.active=prod"]