package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.ReservationInsertDTO;
import gr.aueb.cf.hotel_managment.dto.ReservationReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationRestController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationReadOnlyDTO> createReservation(
            @Valid @RequestBody ReservationInsertDTO dto
    ) {
        try {
            ReservationReadOnlyDTO reservation = reservationService.createReservation(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (AppObjectNotFoundException | AppObjectInvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.noContent().build();
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{id}/dates")
    public ResponseEntity<ReservationReadOnlyDTO> updateReservationDates(
            @PathVariable Long id,
            @RequestParam LocalDate newCheckIn,
            @RequestParam LocalDate newCheckOut
    ) {
        try {
            return ResponseEntity.ok(
                    reservationService.updateReservationDates(id, newCheckIn, newCheckOut)
            );
        } catch (AppObjectNotFoundException | AppObjectInvalidArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
