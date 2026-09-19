package com.staynest.staynest_backend.dto;


import java.io.Serializable;
import java.time.LocalDate;

public record HotelSearchRequest (
        String city,
        LocalDate startDate,
        LocalDate endDate,
        Integer roomsCount,
        Integer page,
        Integer size

) implements Serializable{

    public HotelSearchRequest{
        page = 0;
        size = 10;
    }
}
