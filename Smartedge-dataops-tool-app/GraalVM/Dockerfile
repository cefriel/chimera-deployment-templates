FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /
COPY ./<Folder containing the project source files> /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn clean package


FROM ghcr.io/graalvm/jdk-community:17
WORKDIR /home

RUN microdnf install -y procps

COPY ./SmartEdge-GraalVm/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/<Jar file to run> /home/<Jar file to run>

ENTRYPOINT ["/bin/sh", "-c", "/home/commands.sh"]
