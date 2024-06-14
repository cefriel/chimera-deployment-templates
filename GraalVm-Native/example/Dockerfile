FROM cefriel/native-builder:v17 as builder

WORKDIR /
COPY ./minimal-chimera-app /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn -Pnative -Dagent=false -DskipTests package

FROM alpine:latest

WORKDIR /home

RUN apk update && apk add procps && apk add gcompat

COPY ./GraalVm-Native/example/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/native-minimal-chimera /home/native-minimal-chimera
COPY --from=builder /app/sourceFiles/data /home/data

RUN chmod +x /home/native-minimal-chimera

ENTRYPOINT ["/home/commands.sh"]
