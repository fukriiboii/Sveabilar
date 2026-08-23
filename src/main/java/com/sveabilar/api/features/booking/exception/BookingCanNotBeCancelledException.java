package com.sveabilar.api.features.booking.exception;

public class BookingCanNotBeCancelledException extends RuntimeException {

    public BookingCanNotBeCancelledException(String message){
        super(message); 
    }
    
}
