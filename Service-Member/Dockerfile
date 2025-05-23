# Stage 1: build the app
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: run the app
FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /build/target/Service-Member-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
