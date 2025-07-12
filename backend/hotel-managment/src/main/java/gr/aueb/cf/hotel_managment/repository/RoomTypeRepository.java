package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    Optional<RoomType> findByName(String name);

    @Query("SELECT DISTINCT r.roomType FROM Room r WHERE r.hotel.id = :hotelId")
    List<RoomType> findTypesByHotelId(@Param("hotelId") Long hotelId);
}
