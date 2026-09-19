package com.staynest.staynest_backend.repository;

import com.staynest.staynest_backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
