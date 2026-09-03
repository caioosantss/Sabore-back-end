# Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY receitas/pom.xml .
RUN mvn -B -q dependency:go-offline
COPY receitas/src ./src
RUN mvn -B -q -DskipTests package

# Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
