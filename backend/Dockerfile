FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests dependency:go-offline
RUN mvn -q -DskipTests package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/hospital-management-0.0.1-SNAPSHOT.jar ./hospital-management.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hospital-management.jar"]
