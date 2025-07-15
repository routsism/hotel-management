package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);

    @Query("SELECT r FROM Reservation r WHERE " +
            "r.room.id = :roomId AND " +
            "((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn) OR " +
            "(r.checkInDate >= :checkIn AND r.checkOutDate <= :checkOut))")
    List<Reservation> findConflictingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut
    );
    List<Reservation> findByStatusId(Long statusId);
    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    Page<Reservation> findByRoomId(Long roomId, Pageable pageable);
}
