FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
ADD target/Movieteka-0.0.1-SNAPSHOT.jar movieteka.jar
COPY src/main/resources/init/CategoryInit.csv /app/CategoryInit.csv
EXPOSE 8080
ENTRYPOINT ["java","-jar","/movieteka.jar"]