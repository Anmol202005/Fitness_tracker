FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
EXPOSE 8081
COPY --from=build target/fitness.jar fitness.jar
ENTRYPOINT ["java","-jar","/fitness.jar"]