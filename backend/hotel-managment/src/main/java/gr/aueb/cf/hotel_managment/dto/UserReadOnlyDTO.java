package gr.aueb.cf.hotel_managment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    private Long id;
    private String username;
    private String email;
    private RoleReadOnlyDTO role;
}
