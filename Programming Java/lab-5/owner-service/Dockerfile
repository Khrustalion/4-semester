FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY owner-service/target/owner-service-1.0.0.jar ./owner-service.jar
ENTRYPOINT ["java","-jar","./owner-service.jar"]
