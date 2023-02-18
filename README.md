1. run this project by image

```
./mvnw compile jib:buildTar; \
docker load < target/jib-image.tar; \
docker run -d \
    --name homework-service \
    -e activeProfiles=ide \
    -p 8080:8080 \
    local/homework:latest
```

2. Swagger UI path: http://localhost:8080/swagger-ui/index.html

