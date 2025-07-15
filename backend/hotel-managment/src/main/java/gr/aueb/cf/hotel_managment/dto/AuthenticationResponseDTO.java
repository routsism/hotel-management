package gr.aueb.cf.hotel_managment.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private RoleReadOnlyDTO role;
    private String token;
}