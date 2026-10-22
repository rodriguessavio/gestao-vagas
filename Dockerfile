
FROM ubuntu:latest AS build

WORKDIR /app

RUN apt-get update && apt-get install -y openjdk-17-jdk maven

COPY . .

RUN mvn clean install

FROM eclipse-temurin:17-jdk

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/gestao_vagas-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
