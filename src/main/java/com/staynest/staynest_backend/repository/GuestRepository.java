package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}