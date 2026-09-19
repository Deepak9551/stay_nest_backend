package com.staynest.staynest_backend.controller.admin;

import com.staynest.staynest_backend.dto.HotelDto;
import com.staynest.staynest_backend.services.HotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels")
@Validated
public class HotelController {

    private final HotelService hotelService;


    @PostMapping
    public ResponseEntity<HotelDto> create_hotel(@Valid @RequestBody HotelDto hotelDto){
        hotelDto = hotelService.createNewHotel(hotelDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(hotelDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> get_hotel(@PathVariable(value = "id")  @Positive(message = "Hotel ID must be greater than 0") Long hotel_id){
        HotelDto hotelDto = hotelService.getHotel(hotel_id);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> update_hotel(@PathVariable(value = "id")  @Positive(message = "Hotel ID must be greater than 0") Long hotel_id , @RequestBody HotelDto update_hotel_request){
        HotelDto hotelDto = hotelService.update_hotel(hotel_id, update_hotel_request);

        return ResponseEntity.ok(hotelDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete_hotel(@PathVariable(value = "id") Long hotel_id ){
        hotelService.delete_hotel(hotel_id);

        return ResponseEntity.noContent().build();
    }

    // Path= update a single or few field
    @PatchMapping("/{id}")
    public ResponseEntity<Void> active_hotel(@PathVariable(value = "id") Long hotel_id ){
        hotelService.active_hotel(hotel_id);

        return ResponseEntity.noContent().build();
    }

}
