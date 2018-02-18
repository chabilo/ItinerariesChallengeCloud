package org.challenge.routesservice;

import org.challenge.routesservice.controller.RoutesController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = RoutesController.class)
public class RoutesServiceApplication{   
 
    public static void main(String[] args) {
        SpringApplication.run(RoutesServiceApplication.class, args);
    }
}
