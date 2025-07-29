package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationUpdateDTO;
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

import java.time.LocalDateTime;
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
    public ReservationReadOnlyDTO createReservation(ReservationInsertDTO dto, String username)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));

        ReservationStatus status = reservationStatusRepository.findById(dto.getReservationStatusId())
                .orElseThrow(() -> new AppObjectNotFoundException("Status", "Status not found"));

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                dto.getRoomId(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                null
        );

        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Room already booked");
        }

        Reservation reservation = reservationMapper.toReservationEntity(dto, user, room, status);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(savedReservation);
    }

    @Transactional
    public void cancelReservation(Long id, String username)throws AppObjectNotFoundException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation" ,"Reservation not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User" ,"User not found"));

        if ("ROLE_GUEST".equals(user.getRole().getName()) &&
                !reservation.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Guests can only cancel their own reservations");
        }

        reservationRepository.delete(reservation);
    }

    @Transactional
    public ReservationReadOnlyDTO updateReservationDates(Long id, UpdateReservationDatesDTO dto, String username)throws AppObjectInvalidArgumentException, AppObjectNotFoundException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation","Reservation not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User","User not found"));

        if ("ROLE_GUEST".equals(user.getRole().getName())) {
            if (!reservation.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("Guests can only update their own reservations");
            }
        }

        boolean isOwner = reservation.getUser().getId().equals(user.getId());
        boolean isAdminOrEmployee = isAdminOrEmployee(user);
        String status = reservation.getStatus().getName();

        if (isOwner && !isAdminOrEmployee) {
            if (!status.equals("PENDING")) {
                throw new AccessDeniedException("Οι επισκέπτες μπορούν να τροποποιούν μόνο κρατήσεις σε κατάσταση PENDING");
            }
        }

        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoom().getId(),
                dto.getNewCheckIn(),
                dto.getNewCheckOut(),
                id
        );

        if (!conflicts.isEmpty()) {
            throw new AppObjectInvalidArgumentException("Room", "Το δωμάτιο είναι ήδη κρατημένο για τις νέες ημερομηνίες");
        }

        reservation.setCheckInDate(dto.getNewCheckIn());
        reservation.setCheckOutDate(dto.getNewCheckOut());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updatedReservation);
    }

    @Transactional
    public ReservationReadOnlyDTO updateReservation(Long reservationId, String username, ReservationUpdateDTO dto)throws AppObjectNotFoundException, AccessDeniedException,AppObjectInvalidArgumentException {
        System.out.println("\n=== Attempting update by user: " + username + " ===");
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new AppObjectNotFoundException("Reservation", "Not found"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "Not found"));

        if ("GUEST".equalsIgnoreCase(currentUser.getRole().getName())) {
            System.out.println("[SECURITY BLOCK] Guest user attempted modification");
            throw new AccessDeniedException("Guest users cannot modify reservations");
        }

        boolean isOwner = reservation.getUser().getId().equals(currentUser.getId());
        boolean isAdminOrEmployee = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") ||
                        auth.getAuthority().equals("ROLE_EMPLOYEE"));

        if (!isOwner && !isAdminOrEmployee) {
            throw new AccessDeniedException("Unauthorized access");
        }

        if (dto.getRoomId() != null) {
            Room room = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new AppObjectNotFoundException("Room", "Not found"));
            reservation.setRoom(room);
        }

        if (dto.getCheckInDate() != null) reservation.setCheckInDate(dto.getCheckInDate());
        if (dto.getCheckOutDate() != null) reservation.setCheckOutDate(dto.getCheckOutDate());

        if (reservation.getCheckOutDate().isBefore(reservation.getCheckInDate())) {
            throw new AppObjectInvalidArgumentException("Dates", "Invalid date range");
        }

        Reservation updated = reservationRepository.save(reservation);
        return reservationMapper.toReservationReadOnlyDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<ReservationReadOnlyDTO> getReservationsByUserId(Long userId)
            throws AppObjectNotFoundException {

        if (!userRepository.existsById(userId)) {
            throw new AppObjectNotFoundException("User", "User not found");
        }

        return reservationRepository.findByUserId(userId).stream()
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


