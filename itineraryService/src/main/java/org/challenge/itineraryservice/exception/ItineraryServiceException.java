package org.challenge.itineraryservice.exception;

import org.springframework.http.HttpStatus;

public class ItineraryServiceException extends RuntimeException {

    private static final long serialVersionUID = 9019485638204967642L;

    private final HttpStatus httpStatus;
    private final String message;

    public ItineraryServiceException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
