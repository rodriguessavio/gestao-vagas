
FROM ubuntu:latest AS build

WORKDIR /app

RUN apt-get update && apt-get install -y openjdk-17-jdk maven

COPY . .

RUN mvn clean install && \
    echo "=== JARS ENCONTRADOS ===" && \
    find /app -type f -name "*.jar" && \
    echo "=== CONTEUDO DE /app ===" && \
    ls -la /app && \
    echo "=== TARGET ===" && \
    ls -la /app/target
RUN find /app -type f -name "*.jar"

FROM eclipse-temurin:17-jdk

WORKDIR /app

EXPOSE 8080

COPY --from=build /app/target/gestao_vagas-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
