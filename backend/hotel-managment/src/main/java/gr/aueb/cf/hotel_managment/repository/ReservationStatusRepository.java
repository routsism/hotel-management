package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Long> {

    Optional<ReservationStatus> findByName(String name);

}
