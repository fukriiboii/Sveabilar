package com.sveabilar.api.features.admin.dto;

public record DashboardStatsResponse(
        long totalBookings,
        long todayBookings,
        long upcomingBookings,
        long availableTimes
) {
}