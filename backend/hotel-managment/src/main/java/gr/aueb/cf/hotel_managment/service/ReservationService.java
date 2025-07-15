package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.ReservationMapper;
import gr.aueb.cf.hotel_managment.model.Reservation;
import gr.aueb.cf.hotel_managment.model.ReservationStatus;
import gr.aueb.cf.hotel_managment.model.Room;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.repository.ReservationRepository;
import gr.aueb.cf.hotel_managment.repository.ReservationStatusRepository;
import gr.aueb.cf.hotel_managment.repository.RoomRepository;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    public ReservationReadOnlyDTO createReservation(ReservationInsertDTO dto)
        throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow( () -> new AppObjectNotFoundException("User", "User not found"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow( () -> new AppObjectNotFoundException("Room", "Room not found"));
        ReservationStatus status = reservationStatusRepository.findById(dto.getReservationStatusId())
                .orElseThrow( () -> new AppObjectNotFoundException("Status", "Status not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getRoomId(),
                dto.getCheckInDate().atStartOfDay(),
                dto.getCheckOutDate().atStartOfDay()
        );
        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room is already booked for selected dates");
        }

        Reservation reservation = reservationMapper.toReservationEntity(dto , user, room , status);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(savedReservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) throws AppObjectNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));
        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationReadOnlyDTO updateReservationDates(Long reservationId, LocalDate newCheckIn, LocalDate newCheckOut)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoom().getId(),
                newCheckIn.atStartOfDay(),
                newCheckOut.atStartOfDay()
        );
        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room is already booked for selected dates");
        }

        reservation.setCheckInDate(newCheckIn.atStartOfDay());
        reservation.setCheckOutDate(newCheckOut.atStartOfDay());
        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updatedReservation);
    }
}
