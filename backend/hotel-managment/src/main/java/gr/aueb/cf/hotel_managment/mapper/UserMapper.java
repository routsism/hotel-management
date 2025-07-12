package gr.aueb.cf.hotel_managment.mapper;


import gr.aueb.cf.hotel_managment.dto.UserInsertDTO;
import gr.aueb.cf.hotel_managment.dto.UserReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.Role;
import gr.aueb.cf.hotel_managment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;


    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, RoleMapper roleMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
    }

    public UserReadOnlyDTO toUserReadOnlyDTO(User user) {
        if (user == null) return null;

        UserReadOnlyDTO dto = new UserReadOnlyDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(roleMapper.toRoleReadOnlyDTO(user.getRole()));

        return dto;
    }

    public User toUserEntity(UserInsertDTO dto, Role role) {
        if (dto == null) return null;

        User user = new User();
        user.setRole(role);
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());

        return user;
    }
}
