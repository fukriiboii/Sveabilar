package com.sveabilar.api.features.booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByAvailabilityDate(LocalDate date);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByAvailabilityDateAndStatus(LocalDate date, BookingStatus status);

    boolean existsByAvailabilityIdAndStatus(Long availabilityId, BookingStatus status);

    List<Booking> findByAvailabilityIdAndStatus(Long availabilityId, BookingStatus status);

    List<Booking> findByAvailabilityDateGreaterThanEqualAndStatus(
        LocalDate date,
        BookingStatus status);
    
}