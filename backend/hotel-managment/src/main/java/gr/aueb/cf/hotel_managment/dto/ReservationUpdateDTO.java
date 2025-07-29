package gr.aueb.cf.hotel_managment.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationUpdateDTO {
    @Nullable
    private Long roomId;

    @Nullable
    private Long reservationStatusId;

    @Nullable
    @FutureOrPresent
    private LocalDateTime checkInDate;

    @Nullable
    @Future
    private LocalDateTime checkOutDate;

}