[![Cefriel](https://www.cefriel.com/wp-content/uploads/2022/04/cefriel-logo.svg)](https://www.cefriel.com/)
# SmartEdge dataops tool

The `Chimera-dataops-tool` offer an example of how implements a chimera pipeline using [Apache Camel](https://camel.apache.org/) to harmonize sensors traffic data retrieved from a websocker url.
Three different approaches are shown in the example, which differ in terms of the docker image used and in type of build:
- [SmartEdge-Temurin](SmartEdge-Temurin): SmartEdge app builded with JVM and running on Temurin-17 docker image
- [SmartEdge-GraalVm](SmartEdge-GraalVm): SmartEdge app builded with JVM and rnning on GraalVm-17 docker image (community edition)
- [SmartEdge-Native](SmartEdge-Native): SmartEdge app native-builded with [GraalVm with JDK 17](https://www.graalvm.org/downloads/#) and runnign on Alpine docker image 

The purpose of using the three approaches listed above is to compare the performance of two different JVM builds versus a native build and then shows the advantages in terms of performance and resource usage by using a native executable created with [GraalVm](https://www.graalvm.org/java/advantages/)
The chimera camel pipeline maps the json samples of a traffic radar, retrieved with the websocket url `wss://[websocket-url]?channel=[channel-name]&token=[authentication-token]`, in a RDF output file using the template [template.vm](SmartEdge/Source-Files/src/main/resources/templates/template.vm) 

## Build and run
To build the SmartEdge application you need to use the provided DockerFile and docker-compose.yaml located in the corresponding folder, already listed above. Alternatively you can run the corresponding image hosted on Docker Hub:
- `cefriel/smartedge:temurin-17`
- `cefriel/smartedge:graalvm-17`
- `cefriel/smartedge:native`

To build and to run the images from the source code, go to directory of the example to build and use following docker commands
```
docker-compose build
docker-compose up
```
To run the image hosted on Docker Hub, create a docker-compose.yaml file as specified below:
version: '3'
```
services:
    smart-edge:
        image: [Image name you wants to run]
        container_name: [speciy a container name]
        volumes:
            - ./appFolder:/home/appFolder
            - ./outbox:/home/out
        environment:
            - TZ=Europe/Rome
```
Just for an example, if you wants to run the `temurin-17` image, create a docker-compose.yaml file and copy paste the following instructions:
```
version: '3'

services:
    smart-edge:
        image: cefriel/smartedge:temurin-17
        container_name: smartedge-temurin-17
        volumes:
            - ./appFolder:/home/appFolder
            - ./outbox:/home/out
        environment:
            - TZ=Europe/Rome
```
Run the docker-compose.yaml created with the command
```
docker-compose up
```
## Docker files explanation
### Temurin and GraalVm examples
The dockerfile for building the application that uses the temurin-17 and graalVm-17 JVMs, you can find in [SmartEdge-Temurin/Dockerfile](SmartEdge-Temurin/Dockerfile) and [SmartEdge-GraalVm/Dockerfile](SmartEdge-GraalVm/Dockerfile) respectively, are quite similar. Both or them are designed for building a multi-stage Docker image:
```
FROM maven:3.8.3-openjdk-17 as builder
WORKDIR /
COPY ./Source-Files /app/sourceFiles
WORKDIR /app/sourceFiles
RUN mvn clean package
...
```
As you can see form the docker command shown above, the first stage uses the official Maven image with OpenJDK 17 as the base image `maven:3.8.3-openjdk-17`. The folder containing the project's source files `./Source-Files` is copied into the `/app/sourceFiles` directory within the Docker image and then the maven project is built using the `mvn clean package` command. 

The second phase of the build differs just for the image used:
The Temurin JDK 17 `eclipse-temurin:17` for the temurin-17 example.
```
FROM FROM eclipse-temurin:17
```
And the GraalVM JDK 17 `ghcr.io/graalvm/jdk-community:17` for the graalVm-17 example.
```
FROM ghcr.io/graalvm/jdk-community:17
```

The subsequent docker command are the same for both the examples:
```
RUN apt-get update && apt-get install -y procps
COPY ./SmartEdge-Temurin/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh
COPY --from=builder /app/sourceFiles/target/ChimeraCamelJava-executable.jar /home/ChimeraCamelJava-executable.jar
ENTRYPOINT ["/bin/sh", "-c", "/home/commands.sh"]
```
The `microdnf install -y procps` command is used to install the procps package which will use to retrieve the performance data (CPU and MEMORY usage) after running the application
The `commands.sh` script is copied from the related directory to the `/home` directory in the image and the sript is then given execute permissions using `chmod +x /home/commands.sh`
The executable JAR file `ChimeraCamelJava-executable.jar`, built in the first stage, is copied from `/app/sourceFiles/target/` to `/home` in the second stage.

The entrypoint is set to execute the `/home/commands.sh` script using the shell `/bin/sh -c`.
The `commands.sh` script, you can find in [SmartEdge-Temurin/commands.sh](SmartEdge-Temurin/commands.sh) and in [SmartEdge-GraalVm/commands.sh](SmartEdge-GraalVm/commands.sh) respectively for the temurin and for the graalVm examples, contains basically a set of commands for running the jar file of the related application and some other commandns for retrieve performance data which are saved on a csv file.

### Native example
Also this dockerfile you can find in [SmartEdge-Native/Dockerfile](SmartEdge-Native/Dockerfile) is designed for building a multi-stage Docker image and in this case the first stage uses the `cefriel/native-builder:v17` as base image.
This base imge is just a "wrapper" that put together the required images for building and running a native application builded with graalVM.
It basically contains the `ghcr.io/graalvm/native-image-community:17` image which is required for running a native executable and an `apache-maven-3.9.6` installation for executing the native build.
```
FROM cefriel/native-builder:v17 as builder
WORKDIR /
COPY ./Source-Files /app/sourceFiles
WORKDIR /app/sourceFiles
RUN mvn -Pnative -Dagent=false -DskipTests package
```
Like in the other examples, the folder containing the project's source files `./Source-Files` is copied into the `/app/sourceFiles` directory within the Docker image and than the native build is executed with the command `mvn -Pnative -Dagent=false -DskipTests package`. 
After this command the building stage ends and the second stage starts with the following commands:
```
FROM alpine:latest
WORKDIR /home
RUN apk update && apk add procps && apk add gcompat
COPY ./SmartEdge-Native/commands.sh /home/commands.sh
RUN chmod +x /home/commands.sh
COPY --from=builder /app/sourceFiles/target/chimera-camel-core /home/chimera-camel-core
RUN chmod +x /home/chimera-camel-core
ENTRYPOINT ["/home/commands.sh"]
```
An Alpine Linux's latest version is used as base image for running the native-image builded in the previous step
The `apk update && apk add procps && apk add gcompat` updates the package repository and installs procps and gcompat packages using Alpine package manager. The former is used for the same reason explained beofre and the latter is required for enable the posibility of running the command specified within the ENTRYPOINT.

Also in this case the commands.sh script, located in [SmartEdge-Native/commands.sh](SmartEdge-Native/commands.sh), is copied from the related directory to the `/home` directory in the image and the sript is then given execute permissions using `chmod +x /home/commands.sh`.

The compiled native executable `chimera-camel-core` builded in the first stage is then copied from `/app/sourceFiles/target/chimera-camel-core` to the `/home/chimera-camel-core` of the second stage and the executable is then given execute permission with the `command chmod +x /home/chimera-camel-core`.

The entrypoint is set, like in the other examples, to execute the `/home/commands.sh` which contains the same set of commands for lounch the native executable and to save the performace data in a csv file.

## Application log and output files
By running the application, the chimera pipeline connects to the websocket url specified berore for retrieving the json data of the `lidar.otaniemi.2.json` radar and harmonize the json samples into an RDF files.
These files are located in `/appFolder/data/samples` directory which is created at the application startup and used to store the the RDF files named this way `sample_yyyy-mm-dd_hh-mm-ss_SSS.ttl`.
On the application console log is printed a entry every time a sample il processed by the chimera pipeline, below you can see an example
```
.....
INFO CM=[1] - CPT=[12ms] - SZ=[4968byte] - APT=[12.00ms] - MXPT=12ms] - MNPT=12ms]
INFO CM=[2] - CPT=[15ms] - SZ=[4967byte] - APT=[13.50ms] - MXPT=15ms] - MNPT=12ms]
INFO CM=[3] - CPT=[12ms] - SZ=[4967byte] - APT=[13.00ms] - MXPT=15ms] - MNPT=12ms]
INFO CM=[4] - CPT=[10ms] - SZ=[4963byte] - APT=[12.25ms] - MXPT=15ms] - MNPT=10ms]
INFO CM=[5] - CPT=[11ms] - SZ=[4972byte] - APT=[12.00ms] - MXPT=15ms] - MNPT=10ms]
INFO CM=[6] - CPT=[8ms] - SZ=[4960byte] - APT=[11.33ms] - MXPT=15ms] - MNPT=8ms]
.....
```
The printed values has the following mening:
-   **CM (Count Messages):** Message counter
-   **CPT (Current Porcessing Time):** Time required to harmonize the sample
-   **SZ (Size):** Size of the json sample received from the websocket url
-   **APT (Average Processing Time):** Average processing time related to the number of messages harmonized
-   **MXPT (Maximum Poocessing Time):** Maximun processing time detected for harmonizing a sample
-   **MXPT (Minimum Processing TIME):** Minimum processing time detected for harmonizing a sample

In the directory `/outbox`, which is also create at the application startup, is saved a file named `stats.txt` which contains the perfomance data collected while the application is in runnig. 
Every 5 second the Memory and the CPU usage are stored in this file in a CSV format. Below an example of the file content:
```
Timestamp,MemoryUsage(MB),CPUUsage
2024-03-06 09:27:44,38, 0.0
2024-03-06 09:27:49,116,10.3
2024-03-06 09:27:54,94,18.2
2024-03-06 09:27:59,91,13.4
2024-03-06 09:28:04,91,10.0
2024-03-06 09:28:09,91, 8.0
2024-03-06 09:28:14,91, 6.7
2024-03-06 09:28:19,89, 8.6
2024-03-06 09:28:24,95,11.2
2024-03-06 09:28:29,101,13.2
2024-03-06 09:28:34,108,14.8
2024-03-06 09:28:39,109,16.2
2024-03-06 09:28:44,118,17.3
2024-03-06 09:28:49,118,18.7
2024-03-06 09:28:54,139,19.5
2024-03-06 09:28:59,130,20.2
2024-03-06 09:29:04,140,20.8
.....
```
