package com.sveabilar.api.features.availability.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("AvailabilityRepository Integration Tests")
class AvailabilityRepositoryTest {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        availabilityRepository.deleteAll();

        testDate = LocalDate.of(2026, 8, 20);

        Availability available = createAvailability(
            testDate,
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            AvailabilityStatus.AVAILABLE
        );

        Availability booked = createAvailability(
            testDate,
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            AvailabilityStatus.BOOKED
        );

        Availability otherDate = createAvailability(
            testDate.plusDays(1),
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            AvailabilityStatus.AVAILABLE
        );

        availabilityRepository.saveAll(
            List.of(available, booked, otherDate)
        );
    }

    @Test
    void shouldFindAvailableTimesForDate() {

        List<Availability> result =
            availabilityRepository.findByDateAndAvailabilityStatus(
                testDate,
                AvailabilityStatus.AVAILABLE
            );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStartTime())
            .isEqualTo(LocalTime.of(9, 0));
        assertThat(result.get(0).getEndTime())
            .isEqualTo(LocalTime.of(10, 0));
        assertThat(result.get(0).getAvailabilityStatus())
            .isEqualTo(AvailabilityStatus.AVAILABLE);
    }

    private Availability createAvailability(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        AvailabilityStatus status
    ) {
        Availability availability = new Availability();

        availability.setDate(date);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setAvailabilityStatus(status);
        availability.setCreatedAt(LocalDateTime.now());
        availability.setUpdatedAt(LocalDateTime.now());

        return availability;
    }
}