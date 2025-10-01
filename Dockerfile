# ==== Build Stage ====
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy only pom.xml and download dependencies
# -> Cached unless pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy source & build
# -> This will only break the cache if src/ changes
COPY src ./src
RUN mvn -B clean verify

# ==== Runtime Stage ====
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]