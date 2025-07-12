package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {
    Optional<RoomStatus> findByName(String name);
}
