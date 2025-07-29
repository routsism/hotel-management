package gr.aueb.cf.hotel_managment.security;

import gr.aueb.cf.hotel_managment.authentication.JwtAuthenticationFilter;
import gr.aueb.cf.hotel_managment.authentication.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no auth required)
                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                // Hotel endpoints
                                .requestMatchers(HttpMethod.GET, "/api/hotels", "/api/hotels/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/hotels").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/hotels/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/hotels/**").hasAuthority("ROLE_ADMIN")

                                // Room endpoints
                                .requestMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/rooms").hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")
                                .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")
                                .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")


                                // Reservation endpoints
                                .requestMatchers(HttpMethod.GET, "/api/reservations")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")

                                .requestMatchers(HttpMethod.POST, "/api/reservations")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_GUEST")

                                .requestMatchers(HttpMethod.GET, "/api/reservations/user/*")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_GUEST")

                                .requestMatchers(HttpMethod.PUT, "/api/reservations/*/dates")
                                .hasAuthority("ROLE_GUEST")

                                .requestMatchers(HttpMethod.PUT, "/api/reservations/**")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")

                                .requestMatchers(HttpMethod.DELETE, "/api/reservations/**")
                                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_GUEST")


                                // User endpoints
                                .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                                .requestMatchers("/api/users/**").hasAuthority("ROLE_ADMIN")


                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }


    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}