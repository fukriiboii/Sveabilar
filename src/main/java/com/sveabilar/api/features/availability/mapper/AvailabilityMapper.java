package com.sveabilar.api.features.availability.mapper;

import org.springframework.stereotype.Component;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.dto.AvailabilityTimeRequest;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityRequest;
import com.sveabilar.api.features.availability.dto.UpdateAvailabilityRequest;
import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;

@Component
public class AvailabilityMapper {

    public AvailabilityResponse toResponse(Availability availability) {

        AvailabilityResponse response = new AvailabilityResponse();

        response.setId(availability.getId());
        response.setDate(availability.getDate());
        response.setStartTime(availability.getStartTime());
        response.setEndTime(availability.getEndTime());
        response.setAvailabilityStatus(
                availability.getAvailabilityStatus());

        return response;
    }

    public Availability toEntity(CreateAvailabilityRequest request, AvailabilityTimeRequest time) {

        Availability availability = new Availability();

        availability.setDate(request.getDate());
        availability.setStartTime(time.getStartTime());
        availability.setEndTime(time.getEndTime());
        availability.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        return availability;
    }

    public void updateEntity(
            Availability availability,
            UpdateAvailabilityRequest request) {

        availability.setDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
    }
}