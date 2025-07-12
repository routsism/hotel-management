package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);

    List<Room> findByRoomTypeId(Long roomTypeId);
    List<Room> findByPricePerNightBetween(Double minPrice, Double maxPrice);
    List<Room> findByRoomStatusId(Long statusId);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
            "(SELECT res.room.id FROM Reservation res WHERE " +
            "(res.checkInDate <= :checkOut AND res.checkOutDate >= :checkIn))")
    List<Room> findAvailableRooms(
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut
    );
}
