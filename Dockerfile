FROM openjdk:11
COPY target/coding-challenge-1.0.3.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]