package com.staynest.staynest_backend.dto;

import com.staynest.staynest_backend.entity.HotalContactInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// record class = immutable ( only getter method ) = auto generate ( method , tostring , hash , equals)
public record HotelDto(

        Long id,

        @NotBlank(message = "Hotel name is required")
        String name,

        @NotBlank(message = "City is required")
        String city,

        @NotEmpty(message = "At least one photo is required")
        String[] photos,

        @NotEmpty(message = "At least one amenity is required")
        String[] amenities,

        @NotNull(message = "Contact information is required")
        HotalContactInfo contactInfo,

        @NotNull(message = "Active status is required")
        Boolean active
) {
}