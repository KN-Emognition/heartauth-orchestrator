# docker build -t orchestrator  .
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
COPY contract/ contract/
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jdk-alpine AS jlink
RUN $JAVA_HOME/bin/jlink \
    --module-path $JAVA_HOME/jmods \
    --add-modules java.base,java.logging,java.xml,java.naming,java.sql,java.management,java.instrument,jdk.unsupported,java.desktop,java.security.jgss \
    --output /javaruntime \
    --compress=2 --no-header-files --no-man-pages

FROM alpine:3.22
WORKDIR /app
COPY --from=jlink /javaruntime /opt/java-minimal
ENV PATH="/opt/java-minimal/bin:$PATH"
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]