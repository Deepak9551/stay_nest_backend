package com.staynest.staynest_backend.controller.admin;

import com.staynest.staynest_backend.dto.RoomDto;
import com.staynest.staynest_backend.services.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor

public class RoomController {

    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<RoomDto> create_room(@PathVariable("hotelId") Long hotel_Id,@Valid @RequestBody RoomDto roomDto){

        RoomDto room = roomService.create_room(hotel_Id, roomDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("{roomId}")
    public ResponseEntity<RoomDto> get_room_by_Id(@PathVariable("roomId") Long room_Id){

        RoomDto room = roomService.get_room_by_Id(room_Id);

        return ResponseEntity.ok(room);
    }


    @GetMapping
    public ResponseEntity<List<RoomDto>> get_rooms(@PathVariable("hotelId") Long hotel_Id){

        List<RoomDto> rooms = roomService.get_all_hotel_rooms(hotel_Id);

        return ResponseEntity.ok(rooms);
    }
    @DeleteMapping("{roomId}")
    public ResponseEntity<Void> delete_room(@PathVariable("hotelId") Long hotel_Id,@PathVariable("roomId") Long room_Id){

         roomService.delete_room(hotel_Id,room_Id);

        return ResponseEntity.noContent().build();
    }

}
