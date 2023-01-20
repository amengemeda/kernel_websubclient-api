FROM openjdk:8
EXPOSE 8080
ADD target/kernel-websubclient-api-docker.jar kernel-websubclient-api-docker.jar
ENTRYPOINT ["java", "-jar", "kernel-websubclient-api-docker.jar"]