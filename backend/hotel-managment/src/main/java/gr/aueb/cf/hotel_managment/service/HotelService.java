package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.HotelInsertDTO;
import gr.aueb.cf.hotel_managment.dto.HotelReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.HotelMapper;
import gr.aueb.cf.hotel_managment.model.Hotel;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional
    public Page<HotelReadOnlyDTO> getAllHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(hotelMapper::toHotelReadOnlyDTO);
    }

    @Transactional
    public HotelReadOnlyDTO createHotel(HotelInsertDTO dto) {
        Hotel hotel = hotelMapper.toHotelEntity(dto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toHotelReadOnlyDTO(savedHotel);
    }

    @Transactional(readOnly = true)
    public HotelReadOnlyDTO getHotelById(Long id) throws AppObjectNotFoundException {
        return hotelRepository.findById(id)
                .map(hotelMapper::toHotelReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException("Hotel", "Hotel not found"));
    }

}
