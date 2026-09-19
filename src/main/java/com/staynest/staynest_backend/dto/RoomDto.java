package com.staynest.staynest_backend.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

// record class = immutable ( only getter method ) = auto generate ( method , tostring , hash , equals)
public record RoomDto(

        Long id,

        @NotBlank(message = "Room type is required")
        String type,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be positive")
        BigDecimal price,

        @Size(min = 1, message = "At least one photo is required")
        String[] photos,

        String[] amenities,
        @Size(min = 1, message = "At least one room is required")
         Integer totalCount,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity
) {
}