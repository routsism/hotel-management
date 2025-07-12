package gr.aueb.cf.hotel_managment.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelReadOnlyDTO {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private String email;
}
