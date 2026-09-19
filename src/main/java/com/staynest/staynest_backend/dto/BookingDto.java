package com.staynest.staynest_backend.dto;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.staynest.staynest_backend.entity.enums.BookingStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingDto(
        @NotNull(message = "Room ID cannot be null")
        RoomDto room,

        @NotNull(message = "Hotel ID cannot be null")
        HotelDto hotel,

        @NotNull(message = "Room count is required")
        @Min(value = 1, message = "At least 1 room must be booked")
        Integer roomCount,

        @NotNull(message = "Check-in date is required")
        @FutureOrPresent(message = "Check-in date cannot be in the past")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate,

        List<Long> guestIds,
        BookingStatus status,
        BigDecimal amount
) {
    // Compact constructor for cross-field validation
    public BookingDto {
        if (checkInDate != null && checkOutDate != null && !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check-out date must be strictly after check-in date");
        }
    }
}
