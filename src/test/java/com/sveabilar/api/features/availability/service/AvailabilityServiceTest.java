package com.sveabilar.api.features.availability.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.dto.AvailabilityTimeRequest;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityRequest;
import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.mapper.AvailabilityMapper;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AvailabilityService Unit Tests")
class AvailabilityServiceTest {

        @Mock
        private AvailabilityRepository availabilityRepository;

        @Mock
        private AvailabilityMapper availabilityMapper;

        @InjectMocks
        private AvailabilityServiceImpl availabilityService;

        private LocalDate testDate;

        private Availability availableSlot;

        private AvailabilityResponse availableResponse;

        @BeforeEach
        void setUp() {

                testDate = LocalDate.of(2026, 8, 20);

                availableSlot = new Availability();

                availableSlot.setDate(testDate);
                availableSlot.setStartTime(LocalTime.of(9, 0));
                availableSlot.setEndTime(LocalTime.of(10, 0));
                availableSlot.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

                availableResponse = new AvailabilityResponse();

                availableResponse.setDate(testDate);
                availableResponse.setStartTime(LocalTime.of(9, 0));
                availableResponse.setEndTime(LocalTime.of(10, 0));
                availableResponse.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        }

        @Test
        @DisplayName("Should return available times for selected date")
        void shouldReturnAvailableTimesForDate() {

                // GIVEN
                when(availabilityRepository.findByDateAndAvailabilityStatus(
                                testDate,
                                AvailabilityStatus.AVAILABLE)).thenReturn(List.of(availableSlot));

                when(availabilityMapper.toResponse(availableSlot))
                                .thenReturn(availableResponse);

                // WHEN
                List<AvailabilityResponse> result = availabilityService.getAvailableTimes(testDate);

                // THEN
                assertThat(result)
                                .hasSize(1)
                                .containsExactly(availableResponse);

                verify(availabilityRepository)
                                .findByDateAndAvailabilityStatus(
                                                testDate,
                                                AvailabilityStatus.AVAILABLE);

                verify(availabilityMapper)
                                .toResponse(availableSlot);
        }

        @Test
        @DisplayName("Should return empty list when no available times exist")
        void shouldReturnEmptyListWhenNoAvailableTimesExist() {

                // GIVEN
                LocalDate dateWithoutAvailability = LocalDate.of(2026, 8, 21);

                when(availabilityRepository.findByDateAndAvailabilityStatus(
                                dateWithoutAvailability,
                                AvailabilityStatus.AVAILABLE)).thenReturn(List.of());

                // WHEN
                List<AvailabilityResponse> result = availabilityService.getAvailableTimes(
                                dateWithoutAvailability);

                // THEN
                assertThat(result).isEmpty();

                verify(availabilityRepository)
                                .findByDateAndAvailabilityStatus(
                                                dateWithoutAvailability,
                                                AvailabilityStatus.AVAILABLE);
        }

        @Test
        @DisplayName("Should create multiple availabilities")
        void shouldCreateMultipleAvailabilities() {

                // GIVEN
                AvailabilityTimeRequest first = new AvailabilityTimeRequest();
                first.setStartTime(LocalTime.of(9, 0));
                first.setEndTime(LocalTime.of(9, 30));

                AvailabilityTimeRequest second = new AvailabilityTimeRequest();
                second.setStartTime(LocalTime.of(10, 0));
                second.setEndTime(LocalTime.of(10, 30));

                AvailabilityTimeRequest third = new AvailabilityTimeRequest();
                third.setStartTime(LocalTime.of(11, 0));
                third.setEndTime(LocalTime.of(11, 30));

                CreateAvailabilityRequest request = new CreateAvailabilityRequest();

                request.setDate(testDate);
                request.setTimes(List.of(first, second, third));

                // WHEN
                availabilityService.createAvailability(request);

                // THEN
                verify(availabilityRepository).saveAll(
                                argThat(availabilities -> StreamSupport.stream(availabilities.spliterator(), false).count() == 3));
        }

}