package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.AuthenticationRequestDTO;
import gr.aueb.cf.hotel_managment.dto.AuthenticationResponseDTO;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.model.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import gr.aueb.cf.hotel_managment.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequestDTO request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "Invalid credentials"));

            String jwtToken = jwtService.generateToken(user.getUsername(), user.getRole().getName());

            AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().getName())
                    .token(jwtToken)
                    .build();

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            log.warn("Invalid login attempt for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            log.error("Unexpected error during login for user {}: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Internal server error");
        }
    }
}
