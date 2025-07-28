package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.dto.UpdateReservationDatesDTO;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationMapper reservationMapper;

    private boolean hasRole(User user, String roleNameWithPrefix) {
        return user.getRole().getName().equals(roleNameWithPrefix);
    }

    private boolean isAdminOrEmployee(User user) {
        return hasRole(user, "ROLE_ADMIN") || hasRole(user, "ROLE_EMPLOYEE");
    }

    @Transactional
    public ReservationReadOnlyDTO createReservation(ReservationInsertDTO dto)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));
        ReservationStatus status = reservationStatusRepository.findById(dto.getReservationStatusId())
                .orElseThrow(() -> new AppObjectNotFoundException("Status", "Status not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getRoomId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate()
        );
        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room is already booked for selected dates");
        }

        Reservation reservation = reservationMapper.toReservationEntity(dto, user, room, status);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(savedReservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, String username)
            throws AppObjectNotFoundException, AccessDeniedException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        boolean isOwner = reservation.getUser().getId().equals(user.getId());
        boolean isAdminOrEmployee = isAdminOrEmployee(user);

        if (!isOwner && !isAdminOrEmployee) {
            throw new AccessDeniedException("You are not authorized to cancel this reservation.");
        }

        reservationRepository.deleteById(reservationId);
        reservationRepository.flush();
    }


    @Transactional
    public ReservationReadOnlyDTO updateReservationDates(Long reservationId, String username, UpdateReservationDatesDTO dto)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AccessDeniedException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        boolean isOwner = reservation.getUser().getId().equals(user.getId());
        boolean isAdminOrEmployee = isAdminOrEmployee(user);
        String status = reservation.getStatus().getName();

        if (isOwner && !isAdminOrEmployee) {
            if (!status.equals("PENDING")) {
                throw new AccessDeniedException("Guests can only modify reservations that are PENDING.");
            }
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoom().getId(),
                dto.getNewCheckIn(),
                dto.getNewCheckOut()
        );
        conflicts = conflicts.stream()
                .filter(r -> !r.getId().equals(reservationId))
                .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room is already booked for selected dates");
        }

        reservation.setCheckInDate(dto.getNewCheckIn());
        reservation.setCheckOutDate(dto.getNewCheckOut());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updatedReservation);
    }

    @Transactional
    public ReservationReadOnlyDTO updateReservation(Long reservationId, String username, ReservationInsertDTO dto)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AccessDeniedException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        if (!isAdminOrEmployee(user)) {
            throw new AccessDeniedException("Only admin or employee can update all reservation details.");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));

        ReservationStatus status = reservationStatusRepository.findById(dto.getReservationStatusId())
                .orElseThrow(() -> new AppObjectNotFoundException("Status", "Status not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getRoomId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate()
        );
        conflicts = conflicts.stream()
                .filter(r -> !r.getId().equals(reservationId))
                .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room is already booked for selected dates");
        }

        reservation.setRoom(room);
        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setStatus(status);

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updatedReservation);
    }

    @Transactional
    public List<ReservationReadOnlyDTO> getReservationsByUserId(Long userId)
            throws AppObjectNotFoundException {

        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        return reservations.stream()
                .map(reservationMapper::toReservationReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ReservationReadOnlyDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservationMapper::toReservationReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationReadOnlyDTO updateStatus(Long reservationId, String newStatusName)
            throws AppObjectNotFoundException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Reservation not found"));

        ReservationStatus newStatus = reservationStatusRepository.findByName(newStatusName)
                .orElseThrow(() -> new AppObjectNotFoundException("Status", "Status not found"));

        reservation.setStatus(newStatus);
        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updated);
    }
}


