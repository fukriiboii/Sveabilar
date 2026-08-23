package com.sveabilar.api.features.availability.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;

@DisplayName("AvailabilityMapper Unit Tests")
class AvailabilityMapperTest {

    private final AvailabilityMapper availabilityMapper =
        new AvailabilityMapper();

    @Test
    @DisplayName("Should map Availability entity to AvailabilityResponse")
    void shouldMapEntityToResponse() {

        // GIVEN
        Availability availability = new Availability();

        availability.setId(1L);
        availability.setDate(LocalDate.of(2026, 8, 20));
        availability.setStartTime(LocalTime.of(9, 0));
        availability.setEndTime(LocalTime.of(10, 0));
        availability.setAvailabilityStatus(
            AvailabilityStatus.AVAILABLE
        );

        // WHEN
        AvailabilityResponse response =
            availabilityMapper.toResponse(availability);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDate())
            .isEqualTo(LocalDate.of(2026, 8, 20));
        assertThat(response.getStartTime())
            .isEqualTo(LocalTime.of(9, 0));
        assertThat(response.getEndTime())
            .isEqualTo(LocalTime.of(10, 0));
        assertThat(response.getAvailabilityStatus())
            .isEqualTo(AvailabilityStatus.AVAILABLE);
    }
}