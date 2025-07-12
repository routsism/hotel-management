package gr.aueb.cf.hotel_managment.mapper;

import gr.aueb.cf.hotel_managment.dto.RoleReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {
    public RoleReadOnlyDTO toRoleReadOnlyDTO(Role role) {
        if (role == null) return null;

        RoleReadOnlyDTO dto = new RoleReadOnlyDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        return dto;
    }

}
