package com.sveabilar.api.features.booking.mapper;

import org.springframework.stereotype.Component;

import com.sveabilar.api.features.booking.dto.BookingRequest;
import com.sveabilar.api.features.booking.dto.BookingResponse;
import com.sveabilar.api.features.booking.entity.Booking;

@Component
public class BookingMapper {

    public Booking toEntity(BookingRequest request) {

        Booking booking = new Booking();

        booking.setCustomerName(request.getCustomerName());
        booking.setCustomerEmail(request.getCustomerEmail());
        booking.setCustomerPhone(request.getCustomerPhone());
        booking.setAddress(request.getAddress());
        booking.setServiceType(request.getServiceType());

        return booking;
    }

    public BookingResponse toResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setCustomerName(booking.getCustomerName());
        response.setCustomerEmail(booking.getCustomerEmail());
        response.setCustomerPhone(booking.getCustomerPhone());
        response.setAddress(booking.getAddress());

        response.setBookingDate(
                booking.getAvailability().getDate()
        );

        response.setStartTime(
                booking.getAvailability().getStartTime()
        );

        response.setEndTime(
                booking.getAvailability().getEndTime()
        );

        response.setServiceType(booking.getServiceType());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());

        return response;
    }
}