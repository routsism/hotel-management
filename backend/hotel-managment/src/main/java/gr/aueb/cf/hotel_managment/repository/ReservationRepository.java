package gr.aueb.cf.hotel_managment.repository;

import gr.aueb.cf.hotel_managment.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatusId(Long statusId);

    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    Page<Reservation> findByRoomId(Long roomId, Pageable pageable);

    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.room.id = :roomId
        AND r.id != :excludeReservationId
        AND (
            (r.checkInDate < :checkOut AND r.checkOutDate > :checkIn)
        )
    """)
    List<Reservation> findConflictingReservations(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut,
            @Param("excludeReservationId") Long excludeReservationId
    );

    @Query("SELECT r FROM Reservation r JOIN FETCH r.room WHERE r.id = :id")
    Optional<Reservation> findByIdWithRoom(@Param("id") Long id);

    @Query("""
        SELECT r FROM Reservation r
        WHERE (:userId IS NULL OR r.user.id = :userId)
        AND (:roomId IS NULL OR r.room.id = :roomId)
        AND (:statusId IS NULL OR r.status.id = :statusId)
    """)
    Page<Reservation> findWithFilters(
            @Param("userId") Long userId,
            @Param("roomId") Long roomId,
            @Param("statusId") Long statusId,
            Pageable pageable
    );
}
