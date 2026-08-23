package com.sveabilar.api.features.availability.service;

import java.time.LocalDate;
import java.util.List;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityRequest;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityScheduleRequest;
import com.sveabilar.api.features.availability.dto.UpdateAvailabilityRequest;


public interface AvailabilityService {

    List<AvailabilityResponse> getAvailableTimes(LocalDate date);
    
    // ADMIN
    List<AvailabilityResponse> createAvailability(CreateAvailabilityRequest request); 

    // ADMIN
    List<AvailabilityResponse> getAllAvailabilities();

    // ADMIN
    AvailabilityResponse updateAvailability(Long id, UpdateAvailabilityRequest request);

    //ADMIN
    void deleteAvailability(Long id);

    // ADMIN
    List<AvailabilityResponse> createAvailabilitySchedule(CreateAvailabilityScheduleRequest request);

    // ADMIN
    List<AvailabilityResponse> getAvailabilitiesBetween(LocalDate startDate, LocalDate endDate);

}
