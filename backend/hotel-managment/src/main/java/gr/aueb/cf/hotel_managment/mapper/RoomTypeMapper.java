package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.RoomTypeReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.RoomType;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeMapper {

    public RoomTypeReadOnlyDTO toRoomTypeReadOnlyDTO(RoomType roomType) {
        if (roomType == null) return null;

        RoomTypeReadOnlyDTO dto = new RoomTypeReadOnlyDTO();
        dto.setId(roomType.getId());
        dto.setName(roomType.getName());
        return dto;
    }

    public RoomType toRoomTypeEntity(RoomTypeReadOnlyDTO dto) {
        if (dto == null) return null;

        RoomType roomType = new RoomType();
        roomType.setId(dto.getId());
        roomType.setName(dto.getName());
        return roomType;
    }
}