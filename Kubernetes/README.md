# Running a Chimera project on Kubernetes

The docker image of the chimera project to be used can be specified in the [chimera-converter.yml](./chimera-converter.yml) (exposed port, resources needed/limits, Docker image, labels, etc.). The file creates a Deployment using the converter image for the Pod, and a related Service. We mark the sections of the [Dockerfile](./Dockerfile) and [chimera-converter.yml](./chimera-converter.yml) that require customization with actual project-specific values using the <...> notation.
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
