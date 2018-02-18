package org.challenge.itineraryservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.challenge.itineraryservice.exception.ItineraryServiceException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * {@code Itinerary}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public final class Itinerary {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String originCity;

    private String destinyCity;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;
    
    private Itinerary() {
    }

    public Itinerary(String originCity, String destinyCity, LocalDateTime departureTime, LocalDateTime arrivalTime) {

        if (originCity == null) {
            throw new ItineraryServiceException(BAD_REQUEST, "Origin city can not be null.");
        }

        if (destinyCity == null) {
            throw new ItineraryServiceException(BAD_REQUEST, "Destiny city can not be null.");
        }

        if (departureTime == null) {
            throw new ItineraryServiceException(BAD_REQUEST, "Departure time can not be null.");
        }

        if (arrivalTime == null) {
            throw new ItineraryServiceException(BAD_REQUEST, "Arrival time can not be null.");
        }
        
        this.originCity = originCity;
        this.destinyCity = destinyCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public ObjectId getId() {
        return id;
    }

    public String getOriginCity() {
        return originCity;
    }

    public String getDestinyCity() {
        return destinyCity;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

}
