package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.RoomInsertDTO;
import gr.aueb.cf.hotel_managment.dto.RoomReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    private final RoomTypeMapper roomTypeMapper;
    private final HotelMapper hotelMapper;

    public RoomMapper(RoomTypeMapper roomTypeMapper, HotelMapper hotelMapper) {
        this.roomTypeMapper = roomTypeMapper;
        this.hotelMapper = hotelMapper;
    }

    public RoomReadOnlyDTO toRoomReadOnlyDTO(Room room) {
        if (room == null) return null;

        RoomReadOnlyDTO dto = new RoomReadOnlyDTO();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setPricePerNight(room.getPricePerNight());

        if (room.getRoomType() != null) {
            dto.setRoomType(roomTypeMapper.toRoomTypeReadOnlyDTO(room.getRoomType()));
        }

        if (room.getHotel() != null) {
            dto.setHotel(hotelMapper.toHotelReadOnlyDTO(room.getHotel()));
        }

        return dto;
    }

    public Room toRoomEntity(RoomInsertDTO dto) {
        if (dto == null) return null;

        Room room = new Room();
        room.setRoomNumber(dto.getRoomNumber());
        room.setPricePerNight(dto.getPricePerNight());
        return room;
    }
}