package com.sveabilar.api.features.booking.exception;

public class BookingTermsNotAcceptedException extends RuntimeException {

    public BookingTermsNotAcceptedException(String message) {
        super(message);
    }
}
