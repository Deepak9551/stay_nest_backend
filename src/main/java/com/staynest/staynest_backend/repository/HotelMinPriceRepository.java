package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.dto.HotelPriceDto;
import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.HotelMinPrice;
import com.staynest.staynest_backend.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {

    @Query(
            """
      SELECT new com.staynest.staynest_backend.dto.HotelPriceDto(i.hotel, AVG(i.price))
      FROM HotelMinPrice i
      WHERE i.hotel.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.hotel.active = true
      GROUP BY i.hotel
"""
    )
    Page<HotelPriceDto> find_hotels_with_available_inventory(
            @Param("city") String city,
            @Param("startDate") LocalDate start_date,
            @Param("endDate") LocalDate end_date,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount ,
            Pageable pageable
    );


    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);

}