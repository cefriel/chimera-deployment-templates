FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /
COPY ./minimal-chimera-quarkus-app /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn clean package

FROM eclipse-temurin:17
WORKDIR /home

RUN apt-get update && apt-get install -y procps

COPY ./Temurin/example-quarkus/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/minimal-chimera-quarkus-1.0.0-runner.jar /home/minimal-chimera-quarkus-1.0.0-runner.jar
COPY --from=builder /app/sourceFiles/data /home/data

ENTRYPOINT ["/bin/sh", "-c", "/home/commands.sh"]
