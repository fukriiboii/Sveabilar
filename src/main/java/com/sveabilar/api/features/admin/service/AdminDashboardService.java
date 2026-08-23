package com.sveabilar.api.features.admin.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.sveabilar.api.features.admin.dto.DashboardStatsResponse;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;
import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final BookingRepository bookingRepository;
    private final AvailabilityRepository availabilityRepository;

    public DashboardStatsResponse getStats() {

        LocalDate today = LocalDate.now();

        long totalBookings = bookingRepository.count();

        long todayBookings = bookingRepository
                .findByAvailabilityDateAndStatus(
                        today,
                        BookingStatus.CONFIRMED
                )
                .size();

        long upcomingBookings = bookingRepository
                .findByAvailabilityDateGreaterThanEqualAndStatus(
                        today,
                        BookingStatus.CONFIRMED
                )
                .size();

        long availableTimes = availabilityRepository
                .findByDateAndAvailabilityStatus(
                        today,
                        AvailabilityStatus.AVAILABLE
                )
                .size();

        return new DashboardStatsResponse(
                totalBookings,
                todayBookings,
                upcomingBookings,
                availableTimes
        );
    }
}