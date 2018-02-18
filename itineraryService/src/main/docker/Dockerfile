FROM openjdk:8-jdk-alpine
EXPOSE 8888
VOLUME /tmp
ADD maven/itineraryService-1.0.0.jar itineraryService.jar
RUN sh -c 'touch itineraryService.jar'
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongodb/itineraries","-Djava.security.egd=file:/dev/./urandom","-jar","/itineraryService.jar"]