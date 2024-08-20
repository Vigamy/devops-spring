#
# Build package
#
FROM maven:3.8.3-openjdk-17 AS build
COPY . /app
RUN mvn clean package -DskipTests

#
# Package stage
#

FROM openjdk:17-jdk-slim
COPY --from=build /target/produtoapi-0.0.1-SNAPSHOT.jar /usr/local/lib/produtoapi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "produtoapi.jar"]