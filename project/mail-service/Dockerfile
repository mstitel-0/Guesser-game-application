FROM amazoncorretto:21-alpine
WORKDIR /app

COPY target/mail-service-1.0-SNAPSHOT.jar mail-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "mail-service.jar"]