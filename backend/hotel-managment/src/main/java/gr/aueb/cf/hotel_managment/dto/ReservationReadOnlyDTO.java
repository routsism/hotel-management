package gr.aueb.cf.hotel_managment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationReadOnlyDTO {
    private Long id;
    private String username;
    private String roomNumber;
    private String hotelName;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private ReservationStatusReadOnlyDTO reservationStatus;
}
