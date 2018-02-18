package org.challenge.itineraryservice;

import org.challenge.itineraryservice.controller.ItineraryController;
import org.challenge.itineraryservice.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableConfigServer
@ComponentScan(basePackageClasses = ItineraryController.class)
public class ItineraryServiceApplication {

    @Autowired
    private ItineraryRepository itineraryRepository;

    public static void main(String[] args) {
        SpringApplication.run(ItineraryServiceApplication.class, args);
    }

}
