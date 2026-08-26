package com.sveabilar.api.features.service.dto;

import com.sveabilar.api.features.booking.entity.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceOptionResponse {

    private ServiceType type;
    private String name;
    private String description;
    private Integer price;
    private int durationMinutes;
    private boolean available;
    private boolean requiresQuote;
}
