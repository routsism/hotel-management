package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.RoomInsertDTO;
import gr.aueb.cf.hotel_managment.dto.RoomReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.RoomMapper;
import gr.aueb.cf.hotel_managment.model.Hotel;
import gr.aueb.cf.hotel_managment.model.Room;
import gr.aueb.cf.hotel_managment.model.RoomStatus;
import gr.aueb.cf.hotel_managment.model.RoomType;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.repository.HotelRepository;
import gr.aueb.cf.hotel_managment.repository.RoomRepository;
import gr.aueb.cf.hotel_managment.repository.RoomStatusRepository;
import gr.aueb.cf.hotel_managment.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMapper roomMapper;
    private final RoomStatusRepository roomStatusRepository;


    @Transactional
    public RoomReadOnlyDTO createRoom(RoomInsertDTO dto) throws AppObjectNotFoundException {
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new AppObjectNotFoundException("Hotel", "Hotel not found"));

        RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new AppObjectNotFoundException("RoomType", "Room type not found"));

        Room room = roomMapper.toRoomEntity(dto);
        room.setHotel(hotel);
        room.setRoomType(roomType);


        RoomStatus defaultStatus = roomStatusRepository.findByName("AVAILABLE")
                .orElseThrow(() -> new AppObjectNotFoundException("RoomStatus", "Default status AVAILABLE not found"));
        room.setRoomStatus(defaultStatus);

        Room savedRoom = roomRepository.save(room);
        return roomMapper.toRoomReadOnlyDTO(savedRoom);
    }

    @Transactional
    public RoomReadOnlyDTO updateRoom(Long roomId, RoomInsertDTO dto) throws AppObjectNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));

        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new AppObjectNotFoundException("Hotel", "Hotel not found"));

        RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new AppObjectNotFoundException("RoomType", "Room type not found"));

        room.setRoomNumber(dto.getRoomNumber());
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room.setPricePerNight(dto.getPricePerNight());

        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toRoomReadOnlyDTO(updatedRoom);
    }


    @Transactional(readOnly = true)
    public List<RoomReadOnlyDTO> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toRoomReadOnlyDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomReadOnlyDTO getRoomById(Long id) throws AppObjectNotFoundException {
        return roomRepository.findById(id)
                .map(roomMapper::toRoomReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));
    }


    @Transactional
    public void deleteRoom(Long roomId) throws AppObjectNotFoundException {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppObjectNotFoundException("Room", "Room not found"));

        roomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public List<RoomReadOnlyDTO> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<Room> rooms = roomRepository.findAvailableRooms(
                checkIn.atStartOfDay(),
                checkOut.atStartOfDay()
        );

        return rooms.stream()
                .map(roomMapper::toRoomReadOnlyDTO)
                .toList();
    }
}
