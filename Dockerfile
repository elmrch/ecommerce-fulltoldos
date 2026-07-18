# ── Etapa 1: Build ──
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Node.js: lo necesita el paso de minificación de scrip.js/estilo.css (ver package.json)
RUN apt-get update && apt-get install -y --no-install-recommends nodejs npm && rm -rf /var/lib/apt/lists/*

COPY pom.xml package.json ./
RUN npm install

COPY src ./src
RUN mvn clean package -DskipTests -Pprod-minify

# ── Etapa 2: Run ──
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]