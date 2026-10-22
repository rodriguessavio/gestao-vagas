FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-17-jdk maven
COPY . .
RUN mvn clean install

# Runtime stage
FROM eclipse-temurin:17-jdk-slim
EXPOSE 8080

COPY --from=build /target/gestao-vagas-0.0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]