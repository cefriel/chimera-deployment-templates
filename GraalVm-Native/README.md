# Deploying a Chimera project as a native binary with GraalVm

The [Dockerfile](./Dockerfile) located in this directory serves the
purpose of generating a Docker image containing an executable native
binary built using the [GraalVm JVM](https://www.graalvm.org/) native
builder. This allows Java applications to be deployed on devices that
do not support the JVM while also using fewer resources. We mark the
sections of the Dockerfile that require customization with actual
project-specific values using the <...> notation.

After customizing the Dockerfile, to deploy the associated Chimera
project using Docker, you'll need to execute the
[docker-compose.yaml](./docker-compose.yaml) file.

```
docker-compose build
docker-compose up
```
