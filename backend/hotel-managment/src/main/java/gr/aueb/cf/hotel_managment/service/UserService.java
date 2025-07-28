package gr.aueb.cf.hotel_managment.service;

import gr.aueb.cf.hotel_managment.dto.UserInsertDTO;
import gr.aueb.cf.hotel_managment.dto.UserReadOnlyDTO;
import gr.aueb.cf.hotel_managment.mapper.UserMapper;
import gr.aueb.cf.hotel_managment.model.Role;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectAlreadyExists;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectInvalidArgumentException;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.hotel_managment.repository.RoleRepository;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserReadOnlyDTO createUser(UserInsertDTO dto)
            throws AppObjectAlreadyExists , AppObjectNotFoundException {

        if (userRepository.existsByUsername(dto.getUsername())){
            throw new AppObjectAlreadyExists("User", "Username already exists");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AppObjectAlreadyExists("User", "Email already exists");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new AppObjectNotFoundException("Role", "Role not found"));

        User user = userMapper.toUserEntity(dto, role);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toUserReadOnlyDTO(savedUser);
    }

    @Transactional
    public UserReadOnlyDTO updateUser(Long userId, UserInsertDTO dto)
        throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow( () -> new AppObjectNotFoundException("User", "User not found"));

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) {
            if (dto.getPassword().length() < 8) {
                throw new AppObjectInvalidArgumentException("Password", "Password must be at least 8 characters");
            }
            user.setPassword(passwordEncoder.encode((dto.getPassword())));
        }

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow( () -> new AppObjectNotFoundException("Role", "Role not found"));

            user.setRole(role);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toUserReadOnlyDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    public List<UserReadOnlyDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserReadOnlyDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserById(Long userId) throws AppObjectNotFoundException {
        return userRepository.findById(userId)
                .map(userMapper::toUserReadOnlyDTO)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));
    }

    @Transactional
    public void deleteUser(Long userId) throws AppObjectNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Page<UserReadOnlyDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserReadOnlyDTO);
    }


}
