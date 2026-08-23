package com.sveabilar.api.features.availability.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sveabilar.api.features.auth.security.JwtServiceImpl;
import com.sveabilar.api.features.availability.dto.AvailabilityResponse;
import com.sveabilar.api.features.availability.service.AvailabilityService;

@WebMvcTest(AvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AvailabilityController Unit Tests")
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvailabilityService availabilityService;

    @MockitoBean
    private JwtServiceImpl jwtService;

    @Test
    @DisplayName("Should return 200 OK with available times for selected date")
    void shouldReturn200WithAvailableTimesForSelectedDate() throws Exception {

        // GIVEN
        LocalDate date = LocalDate.of(2026, 8, 20);

        AvailabilityResponse response = new AvailabilityResponse();

        response.setId(1L);
        response.setDate(date);
        response.setStartTime(LocalTime.of(9, 0));
        response.setEndTime(LocalTime.of(10, 0));

        when(availabilityService.getAvailableTimes(date))
                .thenReturn(List.of(response));

        // WHEN + THEN
        mockMvc.perform(
                get("/api/availability")
                        .param("date", "2026-08-20")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2026-08-20"))
                .andExpect(jsonPath("$[0].startTime").value("09:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("10:00:00"));

        verify(availabilityService).getAvailableTimes(date);
    }

    @Test
    @DisplayName("Should return 200 OK with empty list when no available times exist")
    void shouldReturn200WithEmptyListWhenNoAvailableTimesExist() throws Exception {

        // GIVEN
        LocalDate date = LocalDate.of(2026, 8, 21);

        when(availabilityService.getAvailableTimes(date))
                .thenReturn(List.of());

        // WHEN + THEN
        mockMvc.perform(
                get("/api/availability")
                        .param("date", "2026-08-21")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(availabilityService).getAvailableTimes(date);
    }
}