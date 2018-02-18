package org.challenge.routesservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * {@code Route}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public final class Route {

    private String id;
    private String originCity;
    private String destinyCity;
    private String departureTime;
    private String arrivalTime;
    private Integer time;

    private Route() {
    }

    public String getId() {
        return id;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getDestinyCity() {
        return destinyCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public Integer getTime() {
        int time = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        if (arrivalTime != null && departureTime != null) {
            time = (int) ChronoUnit.MINUTES.between(LocalDateTime.parse(departureTime, formatter), LocalDateTime.parse(arrivalTime, formatter));
        }
        return time;
    }

}
