package org.challenge.itineraryservice.repository;

import org.bson.types.ObjectId;
import org.challenge.itineraryservice.model.Itinerary;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ItineraryRepository extends ReactiveCrudRepository<Itinerary, ObjectId> {
    
   @Query("{ 'originCity': ?0, 'destinyCity': ?1}")
  Mono<Boolean> existsByOriginCityAndDestinyCity(String originCity, String destinyCity);
}
