FROM cefriel/native-builder:v17 as builder

WORKDIR /
COPY ./minimal-chimera-spring-app /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn package -Pnative -DskipTests

FROM alpine:latest

WORKDIR /home

RUN apk update && apk add procps && apk add gcompat

COPY ./GraalVm-Native/example-spring/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/minimal-chimera-spring /home/minimal-chimera-spring
COPY --from=builder /app/sourceFiles/data /home/data

RUN chmod +x /home/minimal-chimera-spring

ENTRYPOINT ["/home/commands.sh"]
