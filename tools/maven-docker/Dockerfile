#docker镜像build插件（新版本）
FROM java:alpine
MAINTAINER jwen

VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
