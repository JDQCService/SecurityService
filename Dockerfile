FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=target/SecurityService-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ARG PROFILE=prod
ARG DB_URL
ARG DB_USERNAME
ARG BD_PASSWORD
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=${PROFILE}","--spring.datasource.url=${DB_URL}","--spring.datasource.username=${DB_USERNAME}","--spring.datasource.passwork=${BD_PASSWORD}"]