package com.staynest.staynest_backend.services;

import com.staynest.staynest_backend.dto.BookingDto;
import com.staynest.staynest_backend.dto.BookingRequest;
import com.staynest.staynest_backend.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initialize_booking(BookingRequest bookingRequest);

    BookingDto add_guest( Long booking_id,List<GuestDto> guestDtoList);
}
