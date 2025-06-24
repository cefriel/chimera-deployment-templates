# Deploying a Chimera Route as a Knative Service

This repository demonstrates how to deploy a Chimera route as a serverless application using [Knative](https://knative.dev/), an open-source platform that extends Kubernetes to support serverless workloads.

## Overview

Chimera routes provided as Docker images can be deployed to a Kubernetes environment via Knative Services. Knative enables automatic scaling, traffic routing.

An example Knative Service configuration is included in this repository [](./chimera-knative.yaml)

## What is a Knative Service?

A **Knative Service** is a high-level abstraction that enables developers to deploy and manage stateless containers with minimal configuration. When you deploy a Knative Service:

- Knative automatically creates and manages underlying Kubernetes resources such as Deployments, Services, and Routes.
- A deployed Knative Service is automatically scaled based on request volume, including scaling down to zero when the service is idle.

## Deployment Instructions

To deploy the Chimera route as a Knative Service, follow the steps below.

### Prerequisites

- Access to a Kubernetes cluster with Knative Serving installed and properly configured.
- `kubectl` configured to access your cluster.
- A Docker image of the Chimera route

### Deployment

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
