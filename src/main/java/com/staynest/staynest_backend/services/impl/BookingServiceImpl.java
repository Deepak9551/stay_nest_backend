package com.staynest.staynest_backend.services.impl;

import com.staynest.staynest_backend.dto.BookingDto;
import com.staynest.staynest_backend.dto.BookingRequest;
import com.staynest.staynest_backend.dto.GuestDto;
import com.staynest.staynest_backend.entity.Booking;
import com.staynest.staynest_backend.entity.Guest;
import com.staynest.staynest_backend.entity.Inventory;
import com.staynest.staynest_backend.entity.User;
import com.staynest.staynest_backend.entity.enums.BookingStatus;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.exception.UnAuthorizedException;
import com.staynest.staynest_backend.mapper.BookingMapper;
import com.staynest.staynest_backend.mapper.GuestMapper;
import com.staynest.staynest_backend.repository.*;
import com.staynest.staynest_backend.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.staynest.staynest_backend.advice.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final GuestMapper guestMapper;

    @Override
    @Transactional
    public BookingDto initialize_booking(BookingRequest bookingRequest) {

        log.info("Initialising booking for hostel : {} , room: {} , date {} - {}",
                bookingRequest.hotelId(),bookingRequest.roomId(),bookingRequest.checkInDate(),bookingRequest.checkOutDate());
        var hotel =  hotelRepository.findById(bookingRequest.hotelId())
                .orElseThrow(()-> new ResourceNotFound("hotel","hotel_id", HOTEL_NOT_FOUND));

        var room = roomRepository.findById(bookingRequest.roomId())
                .orElseThrow(() -> new ResourceNotFound("room", "room_Id", ROOM_NOT_FOUND));

        var date_count = ChronoUnit.DAYS.between(bookingRequest.checkInDate(), bookingRequest.checkOutDate())+1;

        List<Inventory> inventoryList = inventoryRepository.find_and_lock_available_inventory(hotel.getCity(), bookingRequest.checkInDate(), bookingRequest.checkOutDate(), bookingRequest.roomsCount());

        System.out.println(date_count);
        System.out.println(inventoryList.size());
        if(inventoryList.size()<date_count){
            throw new IllegalArgumentException("room not available");
        }

        // update the booked count in inventory
        for(Inventory inventory : inventoryList){
            inventory.setReversedCount(inventory.getReversedCount() + bookingRequest.roomsCount());
        }
        inventoryRepository.saveAll(inventoryList);

        // Dummy User


        // create booking
        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .roomCount(bookingRequest.roomsCount())
                .checkInDate(bookingRequest.checkInDate())
                .checkOutDate(bookingRequest.checkOutDate())
                .status(BookingStatus.RESERVED)
                .user(get_current_user())
                .amount(BigDecimal.TEN)
                .build();
     booking =   bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto add_guest(Long booking_id, List<GuestDto> guestDtoList) {

        log.info("Adding guest to the booking with ID: "+booking_id);
        Booking booking = bookingRepository.findById(booking_id)
                .orElseThrow(() -> new ResourceNotFound("booking", "booking", BOOKING_NOT_FOUND));

        User user = get_current_user();
        if(!booking.getUser().equals(user)){
            throw new UnAuthorizedException("User is not the belong to this booking",UNAUTHORIZED_BOOKING);
        }
        if(is_booking_expired(booking)){
            throw new  IllegalStateException("Booking has been expired");
        }
        if(booking.getStatus() != BookingStatus.RESERVED){
            throw new  IllegalStateException("Booking is not under RESERVED state  , so no guest are add");

        }
        for(GuestDto guestDto : guestDtoList){
            guestDto.setUser(get_current_user());
            Guest guest = guestMapper.toEntity(guestDto);

            log.info("guest: {} add to DB ",guestDto.getName());
            guestRepository.save(guest);

            booking.getGuests().add(guest);
            log.info("guest with name {} has been added to the booking with Id {}: ",guestDto.getName(),booking_id);
        }
        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    // check booking is expired or not
    private boolean is_booking_expired(Booking booking){

        LocalDateTime expiration_time = booking.getCreatedAt().plusMinutes(10);
    return LocalDateTime.now().isAfter(expiration_time);
    }


    private User get_current_user(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

