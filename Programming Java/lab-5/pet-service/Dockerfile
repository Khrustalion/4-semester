FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY pet-service/target/pet-service-1.0.0.jar ./pet-service.jar
ENTRYPOINT ["java","-jar","./pet-service.jar"]
