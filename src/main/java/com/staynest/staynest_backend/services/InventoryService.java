package com.staynest.staynest_backend.services;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.HotelSearchRequest;
import com.staynest.staynest_backend.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initialize_rooms_for_a_year(Room room);

    void delete_inventories(Room room);

    Page<HotelDto> search_hotels(HotelSearchRequest hotelSearchRequest);
}
