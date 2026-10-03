package com.staynest.staynest_backend.controller.guest;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.dto.HotelInfo;
import com.staynest.staynest_backend.dto.HotelPriceDto;
import com.staynest.staynest_backend.dto.HotelSearchRequest;
import com.staynest.staynest_backend.services.HotelService;
import com.staynest.staynest_backend.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;

    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> search_hotel(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelPriceDto> hotelDtos = inventoryService.search_hotels(hotelSearchRequest);
     return    ResponseEntity.ok(hotelDtos);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelInfo> get_hotel_Info(@PathVariable("hotelId") Long hotel_Id){
        HotelInfo hotelInfo = hotelService.get_hotel_info(hotel_Id);
        return    ResponseEntity.ok(hotelInfo);
    }
}
