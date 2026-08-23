package com.sveabilar.api.features.availability.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sveabilar.api.features.availability.entity.Availability;
import com.sveabilar.api.features.availability.entity.AvailabilityStatus;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByDateAndAvailabilityStatus(LocalDate date,  AvailabilityStatus status);
    
    boolean existsByDateAndStartTimeAndEndTime(LocalDate date,LocalTime startTime, LocalTime endTime);

    List<Availability> findByDateBetweenOrderByDateAscStartTimeAsc(LocalDate startDate, LocalDate endDate);
}
