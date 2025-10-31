# backend/Dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Leverage cache: download deps first
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Now copy sources and build
COPY src ./src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the fat jar (Spring Boot creates one)
ARG JAR=target/*-SNAPSHOT.jar
COPY --from=build /app/${JAR} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
