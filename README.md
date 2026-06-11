# Chimera Deployment Templates

This repository contains templates to deploy using different approaches offered by [Apache Camel](https://camel.apache.org/) and covering various requirements for cloud and edge environments.

The deployment templates are parametric and practical Chimera pipelines are defined as examples to demonstrate their usage (`chimera-pipelines`) folder.

The folder `chimera-pipelines` contains the following pipelines:
- [minimal-chimera-app](./chimera-pipelines/minimal-chimera-app/): A minimal Chimera pipeline built with Apache Camel Core, demonstrating basic transformation capabilities.
- [minimal-chimera-spring-app](./chimera-pipelines/minimal-chimera-spring-app/): A minimal Chimera pipeline built with the [Spring Boot](https://spring.io/projects/spring-boot) framework.
- [minimal-chimera-quarkus-app](./chimera-pipelines/minimal-chimera-quarkus-app/): A minimal Chimera pipeline built using the [Quarkus](https://quarkus.io/) framework.
- [minimal-chimera-observable-micrometer](./chimera-pipelines/minimal-chimera-observable-micrometer/): A Chimera pipeline enhanced with [Micrometer](https://micrometer.io/) observability features to expose execution metrics.
- [minimal-chimera-kamelet](./chimera-pipelines/minimal-chimera-kamelet/): A basic Kamelet implementation that wraps Chimera components for reuse in other pipelines.

The different deployment options are:
- [Temurin](./Temurin/): Build Chimera pipeline using the [OpenJDK](https://openjdk.org/) JVM and deploy within a container.
- [GraalVM](./GraalVM/): Build Chimera pipeline using the [GraalVM](https://www.graalvm.org/) JVM and deploy within a container.
- [GraalVM Native](./GraalVM-Native/): Build Chimera pipeline using [GraalVM](https://www.graalvm.org/) JVM as native executable and deploy within a container not requiring the JVM.
- [Kubernetes](./Kubernetes/): Manifest files for deployment on [Kubernetes](https://kubernetes.io/) as a Service and instructions for deployment using [Knative](https://knative.dev/).

## Acknowledgements

The development of Chimera deployment templates has been partially supported by the European Commission through the SmartEdge project (Grant Agreement 101092908).

## Contributing

Before contributing, please read carefully, complete and sign our [Contributor Licence Agreement](https://github.com/cefriel/contributing/blob/main/contributor-license-agreement.pdf). 

When contributing to this repository, please first discuss the change you wish to make via issue or any other available method with the repository's owners.
