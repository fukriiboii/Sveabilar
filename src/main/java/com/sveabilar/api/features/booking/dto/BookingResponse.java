package com.sveabilar.api.features.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.entity.ServiceType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponse {

    private Long id;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String address;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private ServiceType serviceType;

    private BookingStatus status;

    private LocalDateTime createdAt;
}