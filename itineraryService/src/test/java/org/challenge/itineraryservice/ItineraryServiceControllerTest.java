package org.challenge.itineraryservice;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.challenge.itineraryservice.model.Itinerary;
import org.challenge.itineraryservice.repository.ItineraryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.String.format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import org.challenge.itineraryservice.controller.ItineraryController;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@RunWith(MockitoJUnitRunner.class)
public class ItineraryServiceControllerTest {

    @Mock
    private ItineraryRepository repository;

    @InjectMocks
    private ItineraryController controller;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Test
    public void returnAllItineraries() {

        // Given
        final List<Itinerary> itineraries = asList(
                new Itinerary("Zaragoza", "Madrid", LocalDateTime.parse("01/01/2017 17:45", formatter), LocalDateTime.parse("01/01/2017 20:15", formatter)),
                new Itinerary("Madrid", "Sevilla", LocalDateTime.parse("01/01/2017 17:45", formatter), LocalDateTime.parse("01/01/2017 21:35", formatter)),
                new Itinerary("Sevilla", "Granada", LocalDateTime.parse("01/01/2017 17:45", formatter), LocalDateTime.parse("01/01/2017 18:35", formatter)),
                new Itinerary("Granada", "Cadiz", LocalDateTime.parse("01/01/2017 17:45", formatter), LocalDateTime.parse("01/01/2017 19:00", formatter))
        );
        when(repository.findAll()).thenReturn(Flux.fromIterable(itineraries));

        // When
        final ResponseEntity<List<Itinerary>> response = controller.getAllItineraries().block();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat((Iterable<Itinerary>) response.getBody()).asList().containsAll(itineraries);
    }

    @Test
    public void returnEmptyBodyWhenNoItineraries() {

        // Given
        when(repository.findAll()).thenReturn(Flux.empty());

        // When
        final ResponseEntity<List<Itinerary>> response = controller.getAllItineraries().block();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void returnItineraryById() {

        // Given
        final Itinerary itinerary = new Itinerary("Zaragoza", "Cadiz", LocalDateTime.parse("01/01/2017 12:00", formatter), LocalDateTime.parse("01/01/2017 20:25", formatter));
        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.just(itinerary));

        // When
        final ResponseEntity<Itinerary> response = controller.getItinerary(ObjectId.get()).block();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(itinerary);
    }

    @Test
    public void returnNotFoundIfItineraryIsNotFound() {

        // Given
        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        // When
        final ResponseEntity<Itinerary> response = controller.getItinerary(ObjectId.get()).block();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void addANewSubscription() {

        // Given
        final Itinerary itinerary = new Itinerary("Zaragoza", "Cadiz", LocalDateTime.parse("01/01/2017 12:00", formatter), LocalDateTime.parse("01/01/2017 20:25", formatter));

        final ObjectId id = ObjectId.get();
        ReflectionTestUtils.setField(itinerary, "id", id);

        when(repository.save(any(Itinerary.class))).thenReturn(Mono.just(itinerary));

        // When
        final ResponseEntity<?> response = controller.addSubscription(itinerary).block();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo(format("/itineraries/%s", id));
    }
}
