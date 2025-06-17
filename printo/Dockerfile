FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/printo-0.0.1-SNAPSHOT.jar app.jar

ENV PORT 8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
