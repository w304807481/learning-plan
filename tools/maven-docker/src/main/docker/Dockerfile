FROM java:alpine
VOLUME /tmp
ADD maven-docker-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
