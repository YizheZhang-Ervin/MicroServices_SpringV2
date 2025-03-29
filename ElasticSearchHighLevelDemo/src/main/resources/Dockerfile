FROM openjdk:8-jdk-alpine
LABEL maintainer=ervinzhang
COPY ./*.jar   /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
