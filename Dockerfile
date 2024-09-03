# Build package
FROM maven:3.8.3-openjdk-19 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:19-jdk-slim
WORKDIR /app
COPY --from=build /app/target/produtoAPI-0.0.1-SNAPSHOT.jar produtoapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "produtoapi.jar"]