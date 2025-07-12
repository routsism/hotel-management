package gr.aueb.cf.hotel_managment.rest;

import gr.aueb.cf.hotel_managment.dto.UserInsertDTO;
import gr.aueb.cf.hotel_managment.dto.UserReadOnlyDTO;
import gr.aueb.cf.hotel_managment.model.core.exceptions.*;
import gr.aueb.cf.hotel_managment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserInsertDTO userInsertDTO,
                                          BindingResult bindingResult) {
        LOGGER.info("Attempting to register new user: {}", userInsertDTO.getUsername());

        if (bindingResult.hasErrors()) {
            LOGGER.warn("Validation errors during registration");
            return ResponseEntity.badRequest().body("Invalid user data");
        }

        try {
            UserReadOnlyDTO createdUser = userService.createUser(userInsertDTO); // Χρησιμοποιήστε τη σωστή μέθοδο
            LOGGER.info("User registered successfully: {}", userInsertDTO.getUsername());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

        } catch (AppObjectAlreadyExists e) {
            LOGGER.warn("Registration failed - user already exists: {}", userInsertDTO.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("Role not found during registration");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Server error during registration: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Registration service unavailable");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        LOGGER.info("Fetching user with ID: {}", id);

        try {
            UserReadOnlyDTO userDTO = userService.getUserById(id);
            return ResponseEntity.ok(userDTO);

        } catch (AppObjectNotFoundException e) {
            LOGGER.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}