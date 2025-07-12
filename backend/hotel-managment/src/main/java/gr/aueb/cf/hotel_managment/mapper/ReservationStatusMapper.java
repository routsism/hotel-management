package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.ReservationStatusReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class ReservationStatusMapper {

    public ReservationStatusReadOnlyDTO toReservationStatusReadOnlyDTO(ReservationStatus status) {
        if (status == null) return null;

        ReservationStatusReadOnlyDTO dto = new ReservationStatusReadOnlyDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());

        return dto;
    }
}
