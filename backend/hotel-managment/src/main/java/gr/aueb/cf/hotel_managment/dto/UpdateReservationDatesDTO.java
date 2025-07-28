package gr.aueb.cf.hotel_managment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateReservationDatesDTO {
    private LocalDateTime newCheckIn;
    private LocalDateTime newCheckOut;
}
