# Chimera Deployment Templates

This repository contains examples of how a chimera pipeline using [Apache Camel](https://camel.apache.org/) can be deployed in several ways.
The different deployment options are:

- [Temurin](./Temurin/): Chimera application built with [OpenJDK](https://openjdk.org/) JVM in two version: [camel-core](./Temurin/example) and [camel-spring](./Temurin/example-spring) and deployed as a Jar
- [GraalVm](./GraalVM/): Chimera application built with the [GraalVm JVM](https://www.graalvm.org/) in two version: [camel-core](./GraalVM/example) and [camel-spring](./GraalVM/example-spring) and deployed as a Jar
- [GraalVm Native](./GraalVm-Native/): Chimera application built with [GraalVm](https://www.graalvm.org/) in two version: [camel-core](./GraalVm-Native/example) and [camel-spring](./GraalVm-Native/example-spring) and deployed as a native executable
- [Kubernetes](./Kubernetes/): Chimera application deployed using [kubernetes](https://kubernetes.io/).


