# Shortest itineraries with microservices

This is a REST API application that calculate the sortest way( in time and in
connections ) to travel from one city to another , independent of the departure time

## How to build

From de root of each service run:

    $ mvn spring-boot:run
    
The order must be:
- itineraryService
- routeService

---

## How to test API REST

* Add a itinerary

    $ curl -v -X POST -H "Content-Type: application/json" http://localhost:8888/itineraries -d '{"origin_city":"Zaragoza","destiny_city":"Madrid","departure_time":"2018-01-01T17:45:00","arrival_time":"2018-01-01T20:15:00"}'
    
    $ curl -v -X POST -H "Content-Type: application/json" http://localhost:8888/itineraries -d   
'{"origin_city":"Madrid","destiny_city":"Sevilla","departure_time":"2018-01-01T17:45:00","arrival_time":"2018-01-01T21:35:00"}'

    $ curl -v -X POST -H "Content-Type: application/json" http://localhost:8888/itineraries -d   
'{"origin_city":"Sevilla","destiny_city":"Granada","departure_time":"2018-01-01T17:45:00","arrival_time":"2018-01-01T18:35:00"}'

  $ curl -v -X POST -H "Content-Type: application/json" http://localhost:8888/itineraries -d   
'{"origin_city":"Granada","destiny_city":"Cadiz","departure_time":"2018-01-01T17:45:00","arrival_time":"2018-01-01T19:00:00"}'


* Get all itineraries

    $ curl -v http://localhost:8888/itineraries

* Get a itinerary

    $ curl -v http://localhost:8080/itineraries/{id}

* Delete a itinerary

    $ curl -v -X DELETE http://localhost:8080/itineraries/{id}

* Get a route given a origin city
  
  $ curl -v http://localhost:8080/routes/{city}
  
  $ curl -v http://localhost:8080/routes/Zaragoza
  
---

## Cloud

The configuration of the API REST is stored in other repository (https://github.com/chabilo/spring-cloud-config).


* Check the configuration server

  $ curl -v http://localhost:8080/routes/service
  
---

## Docker

You can create docker images. From de root of each service (the order doesn't matter) run:

    $ mvn clean package fabric8:build -Pdocker
    
You can start docker containers one-by-one with docker or with docker-compose. From the root of the project run:

    $ docker-compose up -d
    
---

## To-do list

- [ ] Authenticate access to REST api with OAuth
- [ ] Use https
- [x] Use Spring Cloud.
- [x] More unit and integration tests.

