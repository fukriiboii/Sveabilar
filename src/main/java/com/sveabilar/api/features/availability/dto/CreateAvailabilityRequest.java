package com.sveabilar.api.features.availability.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class CreateAvailabilityRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    @Valid
    private List<AvailabilityTimeRequest> times;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<AvailabilityTimeRequest> getTimes() {
        return times;
    }

    public void setTimes(List<AvailabilityTimeRequest> times) {
        this.times = times;
    }
}