package com.sveabilar.api.features.availability.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityRequest;
import com.sveabilar.api.features.availability.dto.CreateAvailabilityScheduleRequest;
import com.sveabilar.api.features.availability.dto.UpdateAvailabilityRequest;
import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;
import com.sveabilar.api.features.availability.exception.AvailabilityNotAvailableException;
import com.sveabilar.api.features.availability.exception.AvailabilityNotFoundException;
import com.sveabilar.api.features.availability.mapper.AvailabilityMapper;
import com.sveabilar.api.features.availability.repository.AvailabilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

        private final AvailabilityRepository availabilityRepository;
        private final AvailabilityMapper availabilityMapper;

        @Override
        @Transactional(readOnly = true)
        public List<AvailabilityResponse> getAvailableTimes(LocalDate date) {

                return availabilityRepository
                                .findByDateAndAvailabilityStatus(
                                                date,
                                                AvailabilityStatus.AVAILABLE)
                                .stream()
                                .map(availabilityMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional
        public List<AvailabilityResponse> createAvailability(
                        CreateAvailabilityRequest request) {

                List<Availability> availabilities = request.getTimes()
                                .stream()
                                .map(time -> availabilityMapper.toEntity(request, time))
                                .toList();

                List<Availability> savedAvailabilities = availabilityRepository.saveAll(availabilities);

                return savedAvailabilities.stream()
                                .map(availabilityMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional
        public List<AvailabilityResponse> createAvailabilitySchedule(
                        CreateAvailabilityScheduleRequest request) {

                validateScheduleRequest(request);

                List<Availability> availabilities = new ArrayList<>();

                LocalDate currentDate = request.getStartDate();

                while (!currentDate.isAfter(request.getEndDate())) {

                        DayOfWeek currentDay = currentDate.getDayOfWeek();

                        if (request.getDaysOfWeek().contains(currentDay)) {

                                LocalTime currentTime = request.getStartTime();

                                while (!currentTime.plusMinutes(
                                                request.getSlotDurationMinutes()).isAfter(request.getEndTime())) {

                                        LocalTime slotEnd = currentTime.plusMinutes(
                                                        request.getSlotDurationMinutes());

                                        boolean exists = availabilityRepository
                                                        .existsByDateAndStartTimeAndEndTime(
                                                                        currentDate,
                                                                        currentTime,
                                                                        slotEnd);

                                        if (!exists) {
                                                Availability availability = new Availability();

                                                availability.setDate(currentDate);
                                                availability.setStartTime(currentTime);
                                                availability.setEndTime(slotEnd);
                                                availability.setAvailabilityStatus(
                                                                AvailabilityStatus.AVAILABLE);

                                                availabilities.add(availability);
                                        }

                                        currentTime = slotEnd;
                                }
                        }

                        currentDate = currentDate.plusDays(1);
                }

                if (availabilities.isEmpty()) {
                        return List.of();
                }

                List<Availability> savedAvailabilities = availabilityRepository.saveAll(availabilities);

                return savedAvailabilities.stream()
                                .map(availabilityMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<AvailabilityResponse> getAllAvailabilities() {

                return availabilityRepository.findAll()
                                .stream()
                                .map(availabilityMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<AvailabilityResponse> getAvailabilitiesBetween(
                        LocalDate startDate,
                        LocalDate endDate) {

                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException(
                                        "Startdatum kan inte vara efter slutdatum");
                }

                return availabilityRepository
                                .findByDateBetweenOrderByDateAscStartTimeAsc(
                                                startDate,
                                                endDate)
                                .stream()
                                .map(availabilityMapper::toResponse)
                                .toList();
        }

        @Override
        @Transactional
        public AvailabilityResponse updateAvailability(
                        Long id,
                        UpdateAvailabilityRequest request) {

                Availability availability = availabilityRepository.findById(id)
                                .orElseThrow(() -> new AvailabilityNotFoundException(
                                                "Tiden existerar inte"));

                availabilityMapper.updateEntity(
                                availability,
                                request);

                Availability updatedAvailability = availabilityRepository.save(availability);

                return availabilityMapper.toResponse(updatedAvailability);
        }

        @Override
        @Transactional
        public void deleteAvailability(Long id) {

                Availability availability = availabilityRepository.findById(id)
                                .orElseThrow(() -> new AvailabilityNotFoundException(
                                                "Hittade ingen tillgänglighet: " + id));

                if (availability.getAvailabilityStatus() == AvailabilityStatus.BOOKED) {

                        throw new AvailabilityNotAvailableException(
                                        "Bokad tid kan inte tas bort");
                }

                availabilityRepository.delete(availability);
        }

        private void validateScheduleRequest(
                        CreateAvailabilityScheduleRequest request) {

                if (request.getStartDate()
                                .isAfter(request.getEndDate())) {

                        throw new IllegalArgumentException(
                                        "Startdatum kan inte vara efter slutdatum");
                }

                if (!request.getStartTime()
                                .isBefore(request.getEndTime())) {

                        throw new IllegalArgumentException(
                                        "Starttid måste vara före sluttid");
                }

                if (request.getSlotDurationMinutes() <= 0) {

                        throw new IllegalArgumentException(
                                        "Tidsintervallet måste vara större än 0 minuter");
                }

                long totalMinutes = java.time.Duration.between(
                                request.getStartTime(),
                                request.getEndTime()).toMinutes();

                if (totalMinutes
                                % request.getSlotDurationMinutes() != 0) {

                        throw new IllegalArgumentException(
                                        "Tidsintervallet måste gå jämnt upp mellan start- och sluttid");
                }

                if (request.getDaysOfWeek() == null
                                || request.getDaysOfWeek().isEmpty()) {

                        throw new IllegalArgumentException(
                                        "Minst en veckodag måste väljas");
                }
        }
}