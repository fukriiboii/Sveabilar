package com.sveabilar.api.features.booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByAvailabilityDate(LocalDate date, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByAvailabilityDateAndStatus(LocalDate date, BookingStatus status, Pageable pageable);

    boolean existsByAvailabilityIdAndStatus(Long availabilityId, BookingStatus status);

    List<Booking> findByAvailabilityIdAndStatus(Long availabilityId, BookingStatus status);

    List<Booking> findByAvailabilityDateGreaterThanEqualAndStatus(
        LocalDate date,
        BookingStatus status);

    long countByAvailabilityDateAndStatus(LocalDate date, BookingStatus status);

    long countByAvailabilityDateGreaterThanEqualAndStatus(LocalDate date, BookingStatus status);
    
}