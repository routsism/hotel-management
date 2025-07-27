package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.RoomInsertDTO;
import gr.aueb.cf.hotel_managment.dto.RoomReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.RoomMapper;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomRestController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;


    @GetMapping
    public ResponseEntity<List<RoomReadOnlyDTO>> getAllRooms() {
        List<RoomReadOnlyDTO> dtos = roomService.getAllRooms();
        return ResponseEntity.ok(dtos);
    }



    @PostMapping
    public ResponseEntity<RoomReadOnlyDTO> createRoom(@RequestBody @Valid RoomInsertDTO dto) {
        try {
            RoomReadOnlyDTO createdRoom = roomService.createRoom(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @GetMapping("/available")
    public ResponseEntity<List<RoomReadOnlyDTO>> findAvailableRooms(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {
        return ResponseEntity.ok(roomService.findAvailableRooms(checkIn, checkOut));
    }


    @PutMapping("/{id}")
    public ResponseEntity<RoomReadOnlyDTO> updateRoom(
            @PathVariable Long id,
            @RequestBody @Valid RoomInsertDTO dto) {
        try {
            RoomReadOnlyDTO updatedRoom = roomService.updateRoom(id, dto);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}

