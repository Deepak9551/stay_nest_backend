package com.staynest.staynest_backend.dto;

import java.time.LocalDate;

public record BookingRequest(
        Long hotelId,
        Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer roomsCount
) {
}
