# ===========================
# 🧱 STAGE 1: Build JAR file
# ===========================
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# Sæt arbejdsmappe
WORKDIR /app
#produktions profil
ENV SPRING_PROFILES_ACTIVE=prod

# Først kopieres kun pom.xml (så dependencies caches)
COPY pom.xml .

# Forhent Maven dependencies – uden at bygge koden
RUN mvn dependency:go-offline -B

# Nu kopieres resten af koden
COPY src ./src

# Byg projektet uden at køre tests
RUN mvn clean package -DskipTests

# ===========================
# 🚀 STAGE 2: Runtime Image
# ===========================
FROM eclipse-temurin:21-jre

# Opret arbejdsmappe inde i containeren
WORKDIR /app

# Kopiér det færdige .jar fra builder-stage
COPY --from=builder /app/target/*.jar app.jar

# Eksponer port (hvis din Spring Boot app bruger fx 8080)
EXPOSE 8080

# Start applikationen
ENTRYPOINT ["java", "-jar", "app.jar"]