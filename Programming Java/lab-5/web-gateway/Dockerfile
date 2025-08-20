FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY web-gateway/target/web-gateway-1.0.0.jar ./web-gateway.jar
ENTRYPOINT ["java","-jar","./web-gateway.jar"]
