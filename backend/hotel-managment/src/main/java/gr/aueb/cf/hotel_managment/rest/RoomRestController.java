package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.RoomInsertDTO;
import gr.aueb.cf.hotel_managment.dto.RoomReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;


    @PostMapping
    public ResponseEntity<RoomReadOnlyDTO> createRoom(@RequestBody RoomInsertDTO dto) {
        try {
            RoomReadOnlyDTO createdRoom = roomService.createRoom(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        } catch (AppObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomReadOnlyDTO>> findAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {
        return ResponseEntity.ok(roomService.findAvailableRooms(checkIn, checkOut));
    }


    @PatchMapping("/{id}/price")
    public ResponseEntity<RoomReadOnlyDTO> updateRoomPrice(
            @PathVariable Long id,
            @RequestParam Double pricePerNight
    ) {
        try {
            RoomReadOnlyDTO updatedRoom = roomService.updateRoomPrice(id, pricePerNight);
            return ResponseEntity.ok(updatedRoom);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomReadOnlyDTO> getRoomById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(roomService.getRoomById(id));
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
