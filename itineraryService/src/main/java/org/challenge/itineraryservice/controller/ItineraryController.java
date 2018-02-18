package org.challenge.itineraryservice.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.challenge.itineraryservice.exception.ItineraryServiceException;
import org.challenge.itineraryservice.model.Itinerary;
import org.challenge.itineraryservice.repository.ItineraryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/itineraries", produces = {APPLICATION_JSON_UTF8_VALUE})
public class ItineraryController {

    private static final Logger log = LoggerFactory.getLogger(ItineraryController.class);

    private ItineraryRepository repository;

    public ItineraryController(ItineraryRepository repository) {
        this.repository = repository;
    }

    /**
     * Query for all itineraries.
     * <p>
     * This method is idempotent.
     *
     * @return HTTP 200 if customers found or HTTP 204 otherwise.
     */
//	@PreAuthorize("#oauth2.hasAnyScope('read','write','read-write')")
    @RequestMapping(method = GET)
    public Mono<ResponseEntity<List<Itinerary>>> getAllItineraries() {
        return repository.findAll().collectList()
                .filter(itineraries -> itineraries.size() > 0)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(noContent().build());
    }

    /**
     * Query for a itinerary with the given Id.
     * <p>
     * This method is idempotent.
     *
     * @param id The id of the customer to look for.
     * @return HTTP 200 if the customer is found or HTTP 404 otherwise.
     */
    public Mono<ResponseEntity<Itinerary>> getItinerary(@PathVariable @NotNull ObjectId id) {
        return repository.findById(id)
                .map(customer -> ok().contentType(APPLICATION_JSON_UTF8).body(customer))
                .defaultIfEmpty(notFound().build());
    }

    /**
     * Create a new itinerary.
     *
     * @param newItinerary The itinerary to create.
     * @return HTTP 201, the header Location contains the URL of the created
     * subscription.
     */
    @RequestMapping(method = POST, consumes = {APPLICATION_JSON_UTF8_VALUE})
    public Mono<ResponseEntity<?>> addSubscription(@RequestBody @Valid Itinerary itinerary) {
        //return repository.save(itinerary).map(saved -> created(URI.create(String.format("/itineraries/%s", saved.getId()))).build());

        return repository.existsByOriginCityAndDestinyCity(itinerary.getOriginCity(), itinerary.getDestinyCity())
                .defaultIfEmpty(Boolean.FALSE)
                .flatMap((exists -> {
                    log.info("exists vale: " + exists);
                    if (exists) {
                        throw new ItineraryServiceException(HttpStatus.BAD_REQUEST, "Itinerary already exists.");
                    } else {
                    return repository
                            .save(itinerary)
                            .map(saved -> created(URI.create(String.format("/itineraries/%s", saved.getId()))).build());
                }}));
    }

    /**
     * Delete a itinerary.
     * <p>
     * This method is idempotent, if it's called multiples times with the same
     * id then the first call will delete the customer and subsequent calls will
     * be silently ignored.
     *
     * @param id The id of the itinerary to delete.
     * @return HTTP 204
     */
    public Mono<ResponseEntity<?>> deleteItinerary(@PathVariable @NotNull ObjectId id) {

        final Mono<ResponseEntity<?>> noContent = Mono.just(noContent().build());

        return repository.existsById(id)
                .filter(Boolean::valueOf)
                .flatMap(exists -> repository.deleteById(id).then(noContent))
                .switchIfEmpty(noContent);
    }
}
