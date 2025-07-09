# Running a Chimera project on Kubernetes

## Deploying a Chimera Route as a Service

The docker image of the chimera project to be used can be specified in the [chimera-converter.yml](./chimera-converter.yml) (exposed port, resources needed/limits, Docker image, labels, etc.). The file creates a Deployment using the converter image for the Pod, and a related Service. We mark the sections of the [Dockerfile](./Dockerfile) and [chimera-converter.yml](./chimera-converter.yml) that require customization with actual project-specific values using the <...> notation. An example of a runnable chimera-converter.yaml kubernetes descriptor is provided [here](./example/chimera-converter.yml).
```
kubectl apply -f chimera-converter.yml
```
If everything is fine, you can run `kubectl get pods` and `kubectl get services` to visualize the running pods. Example:
```
$ kubectl get pods
NAME                             READY   STATUS    RESTARTS   AGE
chimera-example-c6b446c8-7mbg6   1/1     Running   0          33m
```
To manually scale the Service you can run
```
$ kubectl scale --replicas=3 deployments/<name of service>
deployment.apps/chimera-example scaled
$ kubectl get pods
NAME                             READY   STATUS              RESTARTS   AGE
<name of service>                0/1     ContainerCreating   0          2s
<name of service>                0/1     ContainerCreating   0          2s
<name of service>                1/1     Running             0          50m
```

## Deploying a Chimera Route as a Knative Service

This template demonstrates how to deploy a Chimera route as a serverless application using [Knative](https://knative.dev/), an open-source platform that extends Kubernetes to support serverless workloads.

### Overview

Chimera routes provided as Docker images can be deployed to a Kubernetes environment via Knative Services. Knative enables automatic scaling, traffic routing.

An example Knative Service configuration is included in this repository [](./chimera-knative.yaml)

Apache Camel can leverage Knative through the [Camel Knative component](https://camel.apache.org/components/next/knative-component.html) to make Knative aware Chimera pipelines to call and expose services within a Kubernetes cluster. Single routes that use the Knative component can be automatically autoscaled based on demand.

### What is a Knative Service?

A **Knative Service** is a high-level abstraction that enables developers to deploy and manage stateless containers with minimal configuration. When you deploy a Knative Service:

- Knative automatically creates and manages underlying Kubernetes resources such as Deployments, Services, and Routes.
- A deployed Knative Service is automatically scaled based on request volume, including scaling down to zero when the service is idle.

### Deployment Instructions

To deploy the Chimera route as a Knative Service, follow the steps below.

#### Prerequisites

- Access to a Kubernetes cluster with [Knative Serving](https://knative.dev/docs/install/yaml-install/serving/install-serving-with-yaml/#install-the-knative-serving-component) installed and properly configured.
- `kubectl` configured to access your cluster.
- A Docker image of the Chimera route

#### Deployment

1. Modify the template [](./chimera-knative.yaml) file.

2. Apply the Knative Service configuration using `kubectl`:

    ```sh
    kubectl apply -f chimera-knative.yaml
    ```

3. After successful deployment, Knative will provision a route and expose the service via a URL. You can retrieve the URL with:

    ```sh
    kubectl get ksvc
    ```

4. Use `curl`, a web browser, or any HTTP client to access the deployed service via the provided URL.

