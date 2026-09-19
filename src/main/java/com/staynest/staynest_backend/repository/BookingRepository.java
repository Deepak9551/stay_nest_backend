package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
