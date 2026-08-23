package com.sveabilar.api.features.booking.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(String message){
        super(message); 
    }
    
}
