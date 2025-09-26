# docker build -t orchestrator  .
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY interface/ interface/
RUN ./mvnw -B -ntp dependency:go-offline || true

COPY src/ src/
RUN ./mvnw -B -ntp clean package -DskipTests


FROM eclipse-temurin:24-jre
WORKDIR /opt/app
EXPOSE 8080
ARG JAR=target/*-SNAPSHOT.jar
COPY --from=builder /opt/app/${JAR} /opt/app/app.jar

ENTRYPOINT ["java","-jar","/opt/app/app.jar"]