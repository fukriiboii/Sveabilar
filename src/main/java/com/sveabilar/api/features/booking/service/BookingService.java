package com.sveabilar.api.features.booking.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.sveabilar.api.features.booking.dto.BookingRequest;
import com.sveabilar.api.features.booking.dto.BookingResponse;
import com.sveabilar.api.features.booking.entity.BookingStatus;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);
    
    // ADMIN
    void cancelBooking(Long id);
    
    
    // ADMIN
    BookingResponse getBookingById(Long id); 

    // ADMIN
    Page<BookingResponse> getBookings(LocalDate date, BookingStatus status, int page, int size);

  


    
}
