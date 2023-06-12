FROM eclipse-temurin:11-jdk
VOLUME /tmp
ARG JAR_FILE=./build/libs/server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]
EXPOSE 8080