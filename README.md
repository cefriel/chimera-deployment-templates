# Chimera Deployment Templates

This repository contains examples of how a chimera pipeline using [Apache Camel](https://camel.apache.org/) can be deployed in several ways.
The different deployment options are:

- [Temurin](./Temurin/): Chimera application built using [OpenJDK](https://openjdk.org/) JVM in two versions: [camel-core](./Temurin/example) and [camel-spring](./Temurin/example-spring) and deployed as a Jar
- [GraalVM](./GraalVM/): Chimera application built using the [GraalVM JVM](https://www.graalvm.org/) in two versions: [camel-core](./GraalVM/example) and [camel-spring](./GraalVM/example-spring) and deployed as JARs. A third example is built using the [Quarkus](https://quarkus.io/) framework and then run on the GraalVM JVM.
- [GraalVM Native](./GraalVM-Native/): Chimera application built using [GraalVM](https://www.graalvm.org/) in two versions: [camel-core](./GraalVM-Native/example) and [camel-spring](./GraalVM-Native/example-spring) and deployed as a native executable
- [Kubernetes](./Kubernetes/): Chimera application deployed using [kubernetes](https://kubernetes.io/).

The [chimera-kamelet](./chimera-kamelet/) project demonstrates how a Kamelet that wraps Chimera functionality can be deployed as a step within a DataOps pipeline.

The [minimal-chimera-observable-micrometer](./minimal-chimera-observable-micrometer/) DataOps pipeline demonstrates how a pipeline deployed as a JAR using the [Temurin](./Temurin/) deployment template can be enhanced with [Micrometer](https://micrometer.io/) to enable observability and expose valuable execution metrics.
