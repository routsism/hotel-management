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
        dto.setUsername(reservation.getUser().getUsername());
        dto.setRoomNumber(reservation.getRoom().getRoomNumber());
        dto.setHotelName(reservation.getRoom().getHotel().getName());

        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
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
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setStatus(status);
        return reservation;
    }
}