package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
