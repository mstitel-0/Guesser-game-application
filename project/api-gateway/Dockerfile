FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /build

COPY pom.xml .
COPY authentication-service/pom.xml ./authentication-service/
COPY mail-service/pom.xml ./mail-service/
COPY shared-dto/pom.xml ./shared-dto/
COPY api-gateway/pom.xml ./api-gateway/
COPY game-service/pom.xml ./game-service/
COPY telegram-service/pom.xml ./telegram-service/

RUN mvn dependency:go-offline

COPY authentication-service/ ./authentication-service/
COPY mail-service/ ./mail-service/
COPY shared-dto/ ./shared-dto/
COPY api-gateway/ ./api-gateway/
COPY game-service/ ./game-service/
COPY telegram-service/ ./telegram-service/

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /app

COPY --from=build /build/api-gateway/target/api-gateway-1.0-SNAPSHOT.jar api-gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]