package gr.aueb.cf.hotel_managment.rest;


import gr.aueb.cf.hotel_managment.dto.HotelInsertDTO;
import gr.aueb.cf.hotel_managment.dto.HotelReadOnlyDTO;
import gr.aueb.cf.hotel_managment.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
