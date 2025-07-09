# Deploying a Chimera project as a native binary with GraalVm

The [Dockerfile](./Dockerfile) located in this directory serves the
purpose of generating a Docker image containing an executable native
binary built using the [GraalVm JVM](https://www.graalvm.org/) native
builder. This allows Java applications to be deployed on devices that
do not support the JVM while also using fewer resources. We mark the
sections of the Dockerfile that require customization with actual
project-specific values using the <...> notation.

After customizing the Dockerfile, to deploy the associated Chimera
project using Docker, you'll need to follow these instructions:

1. Ensure the native-builder image is present locally, or build it
   ```sh
    docker build -f Dockerfile-native-builder --no-cache -t cefriel/native-builder:v17 .
   ```
2a. [Only if code changed] Build the Native agent image and execute it
   ```sh
    docker build -f Dockerfile-native-agent --no-cache -t cefriel/chimera-deployment-templates:native .
    docker compose up --force-recreate
   ```
2b. [Only if code changed] Wait for the container to process a few message (ensure all routes are executed), then move the generated file in the right folder:
   ```sh
    docker compose down
    docker cp $(docker ps -a -q --filter "name=uc2-dataops"):/app/sourceFiles/target/native/agent-output/main/ ./src/main/resources/META-INF/native-image
   ```
3. Build with the correct Dockerfile. Max memory usage can be set modifying the Dockerfile (default `-XX:MaxHeapSize=256m`).
   ```sh
    docker build -f Dockerfile-native --no-cache -t cefriel/chimera-deployment-templates:native .
   ```
4. Execute the container using the `docker-compose.yaml` file.
    ```sh
    docker-compose up
   ```


The steps 2a and 2b should be executed only if the Java code is changed (**not** needed if only the declarative mapping rules are changed).
