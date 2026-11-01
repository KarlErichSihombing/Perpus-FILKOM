# Tahap 1: build jar dengan Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# Tahap 2: image kecil untuk menjalankan aplikasi
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Free tier hanya punya sekitar 512 MB RAM, jadi batasi heap JVM
ENV JAVA_TOOL_OPTIONS="-Xmx300m"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]