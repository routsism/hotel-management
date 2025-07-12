package gr.aueb.cf.hotel_managment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomInsertDTO {

    @NotEmpty(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotNull(message = "Room type ID is required")
    private Long roomTypeId;

    @NotNull(message = "Price is required")
    private Double pricePerNight;
}
