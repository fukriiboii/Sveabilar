package com.sveabilar.api.features.booking.dto;

import com.sveabilar.api.features.booking.entity.ServiceType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {

    @NotBlank
    private String customerName;

    @NotBlank
    @Email
    private String customerEmail;

    @NotBlank
    private String customerPhone;

    @NotBlank
    private String address;

    @NotNull
    private Long availabilityId;

    @NotNull
    private ServiceType serviceType;

    private Boolean termsAccepted;
}