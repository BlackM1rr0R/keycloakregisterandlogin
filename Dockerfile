# JDK 17 veya Spring Boot'un desteklediği bir sürümü kullanın
FROM openjdk:17-jdk-slim

# JAR dosyasını build/libs dizininden kopyalayacağız
COPY build/libs/keycloackspringboot-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot uygulamasını başlat
ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8086
