FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /
COPY ./minimal-chimera-app /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn clean package

FROM ghcr.io/graalvm/jdk-community:17
WORKDIR /home

RUN microdnf install -y procps

COPY ./GraalVM/example/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/minimal-chimera.jar /home/minimal-chimera.jar
COPY --from=builder /app/sourceFiles/data /home/data

ENTRYPOINT ["/bin/sh", "-c", "/home/commands.sh"]
