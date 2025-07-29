package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationUpdateDTO;
import gr.aueb.cf.hotel_managment.mapper.ReservationStatusMapper;
import gr.aueb.cf.hotel_managment.model.Reservation;
import gr.aueb.cf.hotel_managment.model.ReservationStatus;
import gr.aueb.cf.hotel_managment.model.Room;
import gr.aueb.cf.hotel_managment.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        // User
        if (reservation.getUser() != null) {
            dto.setUsername(reservation.getUser().getUsername());
        }

        if (reservation.getRoom() != null) {
            dto.setRoomId(reservation.getRoom().getId());  // <-- Προσθήκη roomId
            dto.setRoomNumber(reservation.getRoom().getRoomNumber());

            if (reservation.getRoom().getHotel() != null) {
                dto.setHotelName(reservation.getRoom().getHotel().getName());
            }
        }

        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());

        if (reservation.getStatus() != null) {
            dto.setReservationStatus(
                    reservationStatusMapper.toReservationStatusReadOnlyDTO(reservation.getStatus())
            );
        }

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

    public void updateReservationFromDto(ReservationUpdateDTO dto, Reservation reservation) {
        if (dto == null || reservation == null) return;

        if (dto.getCheckInDate() != null) {
            reservation.setCheckInDate(dto.getCheckInDate());
        }
        if (dto.getCheckOutDate() != null) {
            reservation.setCheckOutDate(dto.getCheckOutDate());
        }
    }

    public List<ReservationReadOnlyDTO> toReservationReadOnlyDTOList(List<Reservation> reservations) {
        if (reservations == null) return Collections.emptyList();

        return reservations.stream()
                .map(this::toReservationReadOnlyDTO)
                .collect(Collectors.toList());
    }

    public Reservation toReservationFromUpdateDto(ReservationUpdateDTO dto, Reservation existingReservation) {
        if (dto == null) return null;

        if (existingReservation != null) {
            if (dto.getCheckInDate() != null) {
                existingReservation.setCheckInDate(dto.getCheckInDate());
            }
            if (dto.getReservationStatusId() != null) {
            }
            return existingReservation;
        }

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        return reservation;
    }
}