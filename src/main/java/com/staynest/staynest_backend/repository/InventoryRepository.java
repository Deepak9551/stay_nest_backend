package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.entity.Hotel;
import com.staynest.staynest_backend.entity.Inventory;
import com.staynest.staynest_backend.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    @Modifying
    long deleteByRoomAndDateAfter(Room room, LocalDate date);

    @Modifying
    long deleteByRoom(Room room);

    @Query(
            """
      SELECT DISTINCT i.hotel
      FROM Inventory i
      WHERE i.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.closed = false
        AND (i.totalCount - i.bookedCount - i.reversedCount) >= :roomsCount
      GROUP BY i.hotel, i.room
      HAVING COUNT(i.date) = :dateCount
"""
    )
    Page<Hotel> find_hotel_in_inventory(
            @Param("city") String city,
            @Param("startDate") LocalDate start_date,
            @Param("endDate") LocalDate end_date,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount ,
            Pageable pageable
    );

    @Query("""
        select i    FROM Inventory i
      WHERE i.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.closed = false
        AND (i.totalCount - i.bookedCount - i.reversedCount) >= :roomsCount
""")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> find_and_lock_available_inventory(
            @Param("city") String city,
            @Param("startDate") LocalDate start_date,
            @Param("endDate") LocalDate end_date,
            @Param("roomsCount") Integer roomsCount
    );

}
