package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.ReservationStatusMapper;
import gr.aueb.cf.hotel_managment.model.Reservation;
import gr.aueb.cf.hotel_managment.model.ReservationStatus;
import gr.aueb.cf.hotel_managment.model.Room;
import gr.aueb.cf.hotel_managment.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private final ReservationStatusMapper reservationStatusMapper;

    public ReservationMapper(ReservationStatusMapper reservationStatusMapper) {
        this.reservationStatusMapper = reservationStatusMapper;
    }

    public ReservationReadOnlyDTO toReservationReadOnlyDTO(Reservation reservation) {
        if (reservation == null) return null;

        ReservationReadOnlyDTO dto = new ReservationReadOnlyDTO();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setRoomId(reservation.getRoom().getId());
        dto.setCheckInDate(reservation.getCheckInDate());   // LocalDateTime απευθείας
        dto.setCheckOutDate(reservation.getCheckOutDate()); // LocalDateTime απευθείας
        dto.setReservationStatus(reservationStatusMapper.toReservationStatusReadOnlyDTO(reservation.getStatus()));

        return dto;
    }

    public Reservation toReservationEntity(
            ReservationInsertDTO dto,
            User user,
            Room room,
            ReservationStatus status
    ) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setCheckInDate(dto.getCheckInDate().atStartOfDay());
        reservation.setCheckOutDate(dto.getCheckOutDate().atStartOfDay());
        reservation.setStatus(status);
        return reservation;
    }
}