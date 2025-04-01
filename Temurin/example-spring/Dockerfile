FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /
COPY ./minimal-chimera-spring-app /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn clean package

FROM eclipse-temurin:17
WORKDIR /home

RUN apt-get update && apt-get install -y procps

COPY ./Temurin/example-spring/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/minimal-chimera-spring-0.0.1.jar /home/minimal-chimera-spring-0.0.1.jar
COPY --from=builder /app/sourceFiles/data /home/data

ENTRYPOINT ["/bin/sh", "-c", "/home/commands.sh"]
