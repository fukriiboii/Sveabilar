package com.sveabilar.api.features.availability.exception;

public class AvailabilityNotFoundException extends RuntimeException {

    public AvailabilityNotFoundException(String message) {
        super(message);
    }
}