package com.sveabilar.api.features.availability.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.sveabilar.api.features.availability.entity.AvailabilityStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailabilityResponse {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AvailabilityStatus availabilityStatus;
}