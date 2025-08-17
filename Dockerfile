# docker build -t orchestrator  .
# syntax=docker/dockerfile:1
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY interface/ interface/
RUN ./mvnw -B -ntp dependency:go-offline || true
COPY src/ src/
RUN ./mvnw -B -ntp clean package -DskipTests

# Use JRE for slimmer runtime (or keep 24-jdk if you prefer)
FROM eclipse-temurin:24-jre
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]

