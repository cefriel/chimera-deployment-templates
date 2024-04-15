FROM cefriel/native-builder:v17 as builder

WORKDIR /
COPY ./<Folder containing the project source files> /app/sourceFiles

WORKDIR /app/sourceFiles
RUN mvn -Pnative -Dagent=false -DskipTests package

FROM alpine:latest

WORKDIR /home

RUN apk update && apk add procps && apk add gcompat

COPY ./SmartEdge-Native/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh

COPY --from=builder /app/sourceFiles/target/<Native file to run> /home/<Native file to run>
RUN chmod +x /home/chimera-camel-core

ENTRYPOINT ["/home/commands.sh"]
