# Deploying a Chimera project as a Jar

The [Dockerfile](./Dockerfile) located in this directory serves the
purpose of generating a Docker image containing a Java Jar file for a
Chimera project. We mark the sections of the Dockerfile that require
customization with actual project-specific values using the <...>
notation.

After customizing the Dockerfile, to deploy the associated Chimera
project using Docker, you'll need to execute the
[docker-compose.yaml](./docker-compose.yaml) file.

```
docker-compose build
docker-compose up
```
