package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByNameContainingIgnoreCase(String name);
    List<Hotel> findByAddressContainingIgnoreCase(String location);

    Optional<Hotel> findByEmail(String email);
}
