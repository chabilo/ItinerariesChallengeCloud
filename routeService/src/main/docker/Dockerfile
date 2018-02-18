FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ADD maven/routesService-1.0.0.jar routesService.jar
RUN sh -c 'touch routesService.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/routesService.jar"]
