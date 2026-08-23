package com.sveabilar.api.features.booking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;
import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.entity.ServiceType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BookingRepository Integration Tests")
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Test
    @DisplayName("Should save booking with availability")
    void shouldSaveBookingWithAvailability() {

        // GIVEN
        Availability availability = new Availability();

        availability.setDate(
                LocalDate.of(2026, 8, 20)
        );

        availability.setStartTime(
                LocalTime.of(9, 0)
        );

        availability.setEndTime(
                LocalTime.of(10, 0)
        );

        availability.setAvailabilityStatus(
                AvailabilityStatus.BOOKED
        );

        LocalDateTime now = LocalDateTime.now();

        availability.setCreatedAt(now);
        availability.setUpdatedAt(now);

        Availability savedAvailability =
                availabilityRepository.save(availability);

        Booking booking = new Booking();

        booking.setCustomerName("Test Customer");
        booking.setCustomerEmail("test@example.com");
        booking.setCustomerPhone("0701234567");
        booking.setAddress("Stockholm");

        booking.setAvailability(savedAvailability);

        booking.setServiceType(
                ServiceType.TIRE_CHANGE
        );

        booking.setStatus(
                BookingStatus.CONFIRMED
        );

        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        // WHEN
        Booking savedBooking =
                bookingRepository.save(booking);

        // THEN
        assertThat(savedBooking.getId())
                .isNotNull();

        assertThat(savedBooking.getAvailability())
                .isNotNull();

        assertThat(savedBooking.getAvailability().getId())
                .isEqualTo(savedAvailability.getId());

        assertThat(savedBooking.getAvailability().getDate())
                .isEqualTo(LocalDate.of(2026, 8, 20));

        assertThat(savedBooking.getAvailability().getStartTime())
                .isEqualTo(LocalTime.of(9, 0));

        assertThat(savedBooking.getAvailability().getEndTime())
                .isEqualTo(LocalTime.of(10, 0));

        assertThat(savedBooking.getStatus())
                .isEqualTo(BookingStatus.CONFIRMED);
    }
}