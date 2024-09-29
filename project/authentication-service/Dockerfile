FROM amazoncorretto:21-alpine
WORKDIR /app

COPY target/authentication-service-1.0-SNAPSHOT.jar auth-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "auth-service.jar"]