package com.sveabilar.api.features.availability.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityRequest;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityScheduleRequest;
import com.sveabilar.api.features.availability.dto.UpdateAvailabilityRequest;
import com.sveabilar.api.features.availability.service.AvailabilityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/availabilities")
@RequiredArgsConstructor
public class AdminAvailabilityController {

        private final AvailabilityService availabilityService;

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> createAvailability(
                        @Valid @RequestBody CreateAvailabilityRequest request) {

                availabilityService.createAvailability(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .build();
        }

        @PostMapping("/schedule")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<AvailabilityResponse>> createAvailabilitySchedule(
                        @Valid @RequestBody CreateAvailabilityScheduleRequest request) {

                List<AvailabilityResponse> response = availabilityService.createAvailabilitySchedule(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<AvailabilityResponse>> getAllAvailabilities() {

                return ResponseEntity.ok(
                                availabilityService.getAllAvailabilities());
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AvailabilityResponse> updateAvailability(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateAvailabilityRequest request) {

                AvailabilityResponse response = availabilityService.updateAvailability(id, request);

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteAvailability(
                        @PathVariable Long id) {

                availabilityService.deleteAvailability(id);

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/range")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<AvailabilityResponse>> getAvailabilitiesBetween(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

                return ResponseEntity.ok(
                                availabilityService.getAvailabilitiesBetween(
                                                startDate,
                                                endDate));
        }
}