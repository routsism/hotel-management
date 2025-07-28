package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.HotelInsertDTO;
import gr.aueb.cf.hotel_managment.dto.HotelReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public HotelReadOnlyDTO toHotelReadOnlyDTO(Hotel hotel) {
        if (hotel == null) return null;

        HotelReadOnlyDTO dto = new HotelReadOnlyDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setPhone(hotel.getPhone());
        dto.setEmail(hotel.getEmail());
        return dto;
    }

    public Hotel toHotelEntity(HotelInsertDTO dto) {
        if (dto == null) return null;

        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setPhone(dto.getPhone());
        hotel.setEmail(dto.getEmail());
        return hotel;
    }

    public void updateHotelFromDTO(HotelInsertDTO dto, Hotel hotel) {
        if (dto == null || hotel == null) return;

        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setPhone(dto.getPhone());
        hotel.setEmail(dto.getEmail());
    }

}
