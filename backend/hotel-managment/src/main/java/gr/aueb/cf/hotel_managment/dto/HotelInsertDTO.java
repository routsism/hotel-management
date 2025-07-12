package gr.aueb.cf.hotel_managment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelInsertDTO {

    @NotEmpty(message = "Hotel name is required")
    private String name;

    @NotEmpty(message = "Hotel address is required")
    private String address;

    private String phone;

    private String email;
}
