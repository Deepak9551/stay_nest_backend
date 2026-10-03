package com.staynest.staynest_backend.controller.guest;

import com.staynest.staynest_backend.dto.BookingDto;
import com.staynest.staynest_backend.dto.BookingRequest;
import com.staynest.staynest_backend.dto.GuestDto;
import com.staynest.staynest_backend.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    // API for creating booking
    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialize_booking(@RequestBody BookingRequest bookingRequest){

        BookingDto bookingDto = bookingService.initialize_booking(bookingRequest);
        return ResponseEntity.ok(bookingDto);
    }

    // API for Adding Guest to a Booking
    @PostMapping("/{bookingId}/addguest")
    public ResponseEntity<?> add_guest_to_booking(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList){

      return   ResponseEntity.ok(bookingService.add_guest(bookingId,guestDtoList));
    }
}
