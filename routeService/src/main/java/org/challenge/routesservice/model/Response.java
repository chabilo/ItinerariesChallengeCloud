package org.challenge.routesservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * {@code Response}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public final class Response {

    private String destinyCity;
    private String route;
    private Integer timeInMinutes;

    public Response() {
    }
    
    public Response(String destinyCity, String route, Integer timeInMinutes) {
        this.destinyCity = destinyCity;
        this.route = route;
        this.timeInMinutes = timeInMinutes;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(Integer timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }


    public String getDestinyCity() {
        return destinyCity;
    }

    public void setDestinyCity(String destinyCity)  {
        this.destinyCity = destinyCity;
    }
}
