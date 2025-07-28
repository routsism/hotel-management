package gr.aueb.cf.hotel_managment.rest;


import gr.aueb.cf.hotel_managment.dto.HotelInsertDTO;
import gr.aueb.cf.hotel_managment.dto.HotelReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelRestController {
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<Page<HotelReadOnlyDTO>> getAllHotels(Pageable pageable) {
        return ResponseEntity.ok(hotelService.getAllHotels(pageable));
    }


    @PostMapping
    public ResponseEntity<HotelReadOnlyDTO> createHotel(@Valid @RequestBody HotelInsertDTO dto) {
        HotelReadOnlyDTO createdHotel = hotelService.createHotel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelReadOnlyDTO> getHotelById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(hotelService.getHotelById(id));
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelReadOnlyDTO> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelInsertDTO dto) {
        try {
            HotelReadOnlyDTO updatedHotel = hotelService.updateHotel(id, dto);
            return ResponseEntity.ok(updatedHotel);
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.noContent().build();
        } catch (AppObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
