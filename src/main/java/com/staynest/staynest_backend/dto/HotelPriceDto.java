package com.staynest.staynest_backend.dto;

import com.staynest.staynest_backend.entity.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class HotelPriceDto {

    private Hotel hotel;
    private Double price;

    // Explicit constructor matching: (i.hotel, AVG(i.price))
    public HotelPriceDto(Hotel hotel, Double price) {
        this.hotel = hotel;
        this.price = price;
    }
}