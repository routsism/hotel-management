package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationUpdateDTO;
import gr.aueb.cf.hotel_managment.dto.UpdateReservationDatesDTO;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import gr.aueb.cf.hotel_managment.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationRestController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ReservationReadOnlyDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationReadOnlyDTO>> getReservationsByUserId(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        String loggedUsername = authentication.getName();
        User loggedUser = userRepository.findByUsername(loggedUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!loggedUser.getId().equals(userId) &&
                !hasAdminOrEmployeeRole(loggedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        try {
            return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ReservationReadOnlyDTO> createReservation(
            @Valid @RequestBody ReservationInsertDTO dto,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            ReservationReadOnlyDTO created = reservationService.createReservation(dto, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AppObjectInvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationReadOnlyDTO> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationUpdateDTO dto,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            ReservationReadOnlyDTO updated = reservationService.updateReservation(id, username, dto);
            return ResponseEntity.ok(updated);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AppObjectInvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{id}/dates")
    public ResponseEntity<ReservationReadOnlyDTO> updateReservationDates(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDatesDTO dto,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            ReservationReadOnlyDTO updated = reservationService.updateReservationDates(id, username, dto);
            return ResponseEntity.ok(updated);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AppObjectInvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            reservationService.cancelReservation(id, username);
            return ResponseEntity.noContent().build();
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ReservationReadOnlyDTO> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam String newStatus
    ) {
        try {
            ReservationReadOnlyDTO updated = reservationService.updateStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private boolean hasAdminOrEmployeeRole(User user) {
        String role = user.getRole().getName();
        return role.equals("ROLE_ADMIN") || role.equals("ROLE_EMPLOYEE");
    }
}
