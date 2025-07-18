package gr.aueb.cf.hotel_managment.authentication;

import gr.aueb.cf.hotel_managment.dto.AuthenticationResponseDTO;
import gr.aueb.cf.hotel_managment.dto.AuthenticationRequestDTO;
import gr.aueb.cf.hotel_managment.dto.RoleReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import gr.aueb.cf.hotel_managment.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));


        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().getName());

        return AuthenticationResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(new RoleReadOnlyDTO(user.getRole().getId(), user.getRole().getName()))
                .token(token)
                .build();
    }
}