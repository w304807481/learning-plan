#docker镜像build插件（老版本）
FROM java:alpine
MAINTAINER jwen

VOLUME /tmp
ADD maven-docker-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
