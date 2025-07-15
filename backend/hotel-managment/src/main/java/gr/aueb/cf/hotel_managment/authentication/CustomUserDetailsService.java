package gr.aueb.cf.hotel_managment.authentication;


import gr.aueb.cf.hotel_managment.model.Role;
import gr.aueb.cf.hotel_managment.model.User;
import gr.aueb.cf.hotel_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        List<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRole());


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private List<SimpleGrantedAuthority> mapRolesToAuthorities(Role role) {
        String roleName = "ROLE_" + role.getName().toUpperCase();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));

    }
}