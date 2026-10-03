package com.staynest.staynest_backend.services.impl;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.HotelPriceDto;
import com.staynest.staynest_backend.dto.HotelSearchRequest;
import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.Inventory;
import com.staynest.staynest_backend.entity.Room;
import com.staynest.staynest_backend.mapper.HotelMapper;
import com.staynest.staynest_backend.repository.HotelMinPriceRepository;
import com.staynest.staynest_backend.repository.InventoryRepository;
import com.staynest.staynest_backend.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final HotelMapper hotelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void initialize_rooms_for_a_year(Room room) {

        var today = LocalDate.now(); // Day 1
        var end_date = today.plusYears(1); // Day 1 + Day 356

        for (; !today.isAfter(end_date) ;today= today.plusDays(1)) {

            Inventory inventory = Inventory.builder()
                    .room(room)
                    .hotel(room.getHotel())
                    .date(today)
                    .city(room.getHotel().getCity())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .price(room.getBasePrice())
                    .bookedCount(0)
                    .reversedCount(0)
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);


        }

    }

    public void delete_inventories(Room room){
//        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> search_hotels(HotelSearchRequest hotelSearchRequest) {
        PageRequest pageRequest = PageRequest.of(hotelSearchRequest.page(),hotelSearchRequest.size());
        long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.startDate(), hotelSearchRequest.endDate()) + 1;
        Page<Hotel> hotelPage = inventoryRepository.find_hotel_in_inventory(hotelSearchRequest.city(), hotelSearchRequest.startDate(), hotelSearchRequest.endDate(), hotelSearchRequest.roomsCount(), dateCount, pageRequest);

        // business logic - 90 days
        Page<HotelPriceDto> page = hotelMinPriceRepository.find_hotels_with_available_inventory(
                hotelSearchRequest.city(),
                hotelSearchRequest.startDate(),
                hotelSearchRequest.endDate(),
                hotelSearchRequest.roomsCount(),
                dateCount,
                pageRequest
        );
        return page;

    }
}
