package com.sveabilar.api.features.booking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.exception.AvailabilityNotAvailableException;
import com.sveabilar.api.features.availability.exception.AvailabilityNotFoundException;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;
import com.sveabilar.api.features.booking.dto.BookingRequest;
import com.sveabilar.api.features.booking.dto.BookingResponse;
import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.entity.ServiceType;
import com.sveabilar.api.features.booking.mapper.BookingMapper;
import com.sveabilar.api.features.booking.repository.BookingRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService Unit Tests")
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequest request;
    private Availability availability;
    private Booking booking;
    private BookingResponse response;

    @BeforeEach
    void setUp() {

        availability = new Availability();

        availability.setId(1L);
        availability.setDate(LocalDate.of(2026, 8, 20));
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(10, 0));
        availability.setAvailabilityStatus(
                AvailabilityStatus.AVAILABLE
        );

        request = new BookingRequest();

        request.setCustomerName("Fahri");
        request.setCustomerEmail("fahri@example.com");
        request.setCustomerPhone("0701234567");
        request.setAddress("Stockholm");
        request.setAvailabilityId(1L);
        request.setServiceType(ServiceType.TIRE_CHANGE);

        booking = new Booking();
        booking.setId(10L);

        response = new BookingResponse();
        response.setId(10L);
    }

    @Test
    @DisplayName("Should create confirmed booking for available time")
    void shouldCreateConfirmedBookingForAvailableTime() {

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        when(bookingMapper.toEntity(request))
                .thenReturn(booking);

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        when(bookingMapper.toResponse(booking))
                .thenReturn(response);

        BookingResponse result =
                bookingService.createBooking(request);

        assertThat(result).isSameAs(response);

        assertThat(booking.getAvailability())
                .isSameAs(availability);

        assertThat(booking.getStatus())
                .isEqualTo(BookingStatus.CONFIRMED);

        assertThat(availability.getAvailabilityStatus())
                .isEqualTo(AvailabilityStatus.BOOKED);

        verify(availabilityRepository)
                .findById(1L);

        verify(bookingRepository)
                .save(booking);

        verify(bookingMapper)
                .toEntity(request);

        verify(bookingMapper)
                .toResponse(booking);
    }

    @Test
    @DisplayName("Should throw AvailabilityNotFoundException when availability does not exist")
    void shouldThrowWhenAvailabilityDoesNotExist() {

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bookingService.createBooking(request)
        )
                .isInstanceOf(AvailabilityNotFoundException.class)
                .hasMessage("Availability not found");
    }

    @Test
    @DisplayName("Should throw AvailabilityNotAvailableException when availability is not available")
    void shouldThrowWhenAvailabilityIsNotAvailable() {

        availability.setAvailabilityStatus(
                AvailabilityStatus.BOOKED
        );

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        assertThatThrownBy(() ->
                bookingService.createBooking(request)
        )
                .isInstanceOf(AvailabilityNotAvailableException.class)
                .hasMessage("The selected time is no longer available");
    }
}