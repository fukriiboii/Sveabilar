package com.sveabilar.api.features.booking.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.exception.AvailabilityNotAvailableException;
import com.sveabilar.api.features.availability.exception.AvailabilityNotFoundException;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;
import com.sveabilar.api.features.booking.dto.BookingRequest;
import com.sveabilar.api.features.booking.dto.BookingResponse;
import com.sveabilar.api.features.booking.entity.Booking;
import com.sveabilar.api.features.booking.entity.BookingStatus;
import com.sveabilar.api.features.booking.exception.BookingCanNotBeCancelledException;
import com.sveabilar.api.features.booking.exception.BookingNotFoundException;
import com.sveabilar.api.features.booking.exception.BookingTermsNotAcceptedException;
import com.sveabilar.api.features.booking.mapper.BookingMapper;
import com.sveabilar.api.features.booking.repository.BookingRepository;
import com.sveabilar.api.features.email.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final AvailabilityRepository availabilityRepository;
    private final BookingMapper bookingMapper;

    private final EmailService emailService; 

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        if (!Boolean.TRUE.equals(request.getTermsAccepted())) {
            throw new BookingTermsNotAcceptedException("Du måste godkänna bokningsvillkoren");
        }

        Availability availability = availabilityRepository
                .findById(request.getAvailabilityId())
                .orElseThrow(() -> new AvailabilityNotFoundException(
                        "Den valda tiden kunde inte hittas"));

        if (availability.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new AvailabilityNotAvailableException(
                    "Den valda tiden är inte längre tillgänglig");
        }

        boolean alreadyBooked = bookingRepository.existsByAvailabilityIdAndStatus(
                availability.getId(),
                BookingStatus.CONFIRMED);

        if (alreadyBooked) {
            throw new AvailabilityNotAvailableException(
                    "Den valda tiden är redan bokad");
        }

        Booking booking = bookingMapper.toEntity(request);

        booking.setAvailability(availability);
        booking.setStatus(BookingStatus.CONFIRMED);

        availability.setAvailabilityStatus(AvailabilityStatus.BOOKED);

        Booking savedBooking = bookingRepository.save(booking);

        String serviceName = savedBooking.getServiceType().getDisplayName();

        try {
            emailService.sendBookingConfirmation(
                savedBooking.getCustomerEmail(),
                savedBooking.getCustomerName(),
                serviceName,
                savedBooking.getAvailability().getDate().toString(),
                savedBooking.getAvailability().getStartTime() + " - " + savedBooking.getAvailability().getEndTime(),
                savedBooking.getAddress()
            );
        } catch (Exception exception) {
            logger.error(
                    "Booking confirmation email failed for bookingId={} recipient={}",
                    savedBooking.getId(),
                    savedBooking.getCustomerEmail(),
                    exception);
        }

        return bookingMapper.toResponse(savedBooking);
    }

    // ADMIN
    @Transactional
    @Override
    public void cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingCanNotBeCancelledException("Bokning hittades inte med id: " + id));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingCanNotBeCancelledException("Bokningen är redan avbryten");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingCanNotBeCancelledException("Bokningen är redan genomförd");
        }

        Availability availability = booking.getAvailability();

        booking.setStatus(BookingStatus.CANCELLED);

        boolean hasActiveBooking = bookingRepository.existsByAvailabilityIdAndStatus(
                availability.getId(),
                BookingStatus.CONFIRMED);

        if (!hasActiveBooking) {
            availability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        } else {
            availability.setAvailabilityStatus(AvailabilityStatus.BOOKED);
        }

        bookingRepository.save(booking);
        availabilityRepository.save(availability);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingResponse getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Bokningen existerar inte: " + id));

        return bookingMapper.toResponse(booking);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookingResponse> getBookings(
            LocalDate date,
            BookingStatus status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.asc("availability.date"),
                        Sort.Order.asc("availability.startTime")));

        Page<Booking> bookings;

        if (date != null && status != null) {
            bookings = bookingRepository
                    .findByAvailabilityDateAndStatus(date, status, pageable);

        } else if (date != null) {
            bookings = bookingRepository
                    .findByAvailabilityDate(date, pageable);

        } else if (status != null) {
            bookings = bookingRepository
                    .findByStatus(status, pageable);

        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        return bookings.map(bookingMapper::toResponse);
    }

    

}