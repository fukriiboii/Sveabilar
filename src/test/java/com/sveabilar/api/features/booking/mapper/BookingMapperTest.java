package com.sveabilar.api.features.booking.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.booking.dto.BookingRequest;
import com.sveabilar.api.features.booking.dto.BookingResponse;
import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.entity.ServiceType;

@DisplayName("BookingMapper Unit Tests")
class BookingMapperTest {

    private final BookingMapper bookingMapper = new BookingMapper();

    @Nested
    @DisplayName("BookingRequest to Booking")
    class ToEntity {

        @Test
        @DisplayName("Should map customer and service fields from request")
        void shouldMapCustomerAndServiceFieldsToEntity() {

            // GIVEN
            BookingRequest request = new BookingRequest();

            request.setCustomerName("Fahri");
            request.setCustomerEmail("fahri@example.com");
            request.setCustomerPhone("0701234567");
            request.setAddress("Stockholm");
            request.setAvailabilityId(1L);
            request.setServiceType(ServiceType.TIRE_CHANGE);

            // WHEN
            Booking booking = bookingMapper.toEntity(request);

            // THEN
            assertThat(booking.getCustomerName())
                    .isEqualTo("Fahri");

            assertThat(booking.getCustomerEmail())
                    .isEqualTo("fahri@example.com");

            assertThat(booking.getCustomerPhone())
                    .isEqualTo("0701234567");

            assertThat(booking.getAddress())
                    .isEqualTo("Stockholm");

            assertThat(booking.getServiceType())
                    .isEqualTo(ServiceType.TIRE_CHANGE);

            // Availability ska sättas i service-lagret,
            // inte av mapper från ett availabilityId.
            assertThat(booking.getAvailability())
                    .isNull();
        }
    }

    @Nested
    @DisplayName("Booking to BookingResponse")
    class ToResponse {

        @Test
        @DisplayName("Should map booking and availability fields to response")
        void shouldMapBookingAndAvailabilityFieldsToResponse() {

            // GIVEN
            Availability availability = new Availability();

            availability.setId(1L);
            availability.setDate(LocalDate.of(2026, 8, 20));
            availability.setStartTime(LocalTime.of(9, 0));
            availability.setEndTime(LocalTime.of(10, 0));
            availability.setAvailabilityStatus(
                    AvailabilityStatus.AVAILABLE
            );

            Booking booking = new Booking();

            booking.setId(1L);
            booking.setCustomerName("Fahri");
            booking.setCustomerEmail("fahri@example.com");
            booking.setCustomerPhone("0701234567");
            booking.setAddress("Stockholm");
            booking.setAvailability(availability);
            booking.setServiceType(ServiceType.TIRE_CHANGE);
            booking.setStatus(BookingStatus.CONFIRMED);

            LocalDateTime createdAt = LocalDateTime.of(
                    2026, 8, 8, 15, 0
            );

            booking.setCreatedAt(createdAt);

            // WHEN
            BookingResponse response =
                    bookingMapper.toResponse(booking);

            // THEN
            assertThat(response.getId())
                    .isEqualTo(1L);

            assertThat(response.getCustomerName())
                    .isEqualTo("Fahri");

            assertThat(response.getCustomerEmail())
                    .isEqualTo("fahri@example.com");

            assertThat(response.getCustomerPhone())
                    .isEqualTo("0701234567");

            assertThat(response.getAddress())
                    .isEqualTo("Stockholm");

            assertThat(response.getBookingDate())
                    .isEqualTo(LocalDate.of(2026, 8, 20));

            assertThat(response.getStartTime())
                    .isEqualTo(LocalTime.of(9, 0));

            assertThat(response.getEndTime())
                    .isEqualTo(LocalTime.of(10, 0));

            assertThat(response.getServiceType())
                    .isEqualTo(ServiceType.TIRE_CHANGE);

            assertThat(response.getStatus())
                    .isEqualTo(BookingStatus.CONFIRMED);

            assertThat(response.getCreatedAt())
                    .isEqualTo(createdAt);
        }
    }
}