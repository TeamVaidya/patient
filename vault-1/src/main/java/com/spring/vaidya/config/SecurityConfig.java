package com.spring.vaidya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.vaidya.jwt.JwtAuthFilter;

import java.util.List;

/**
 * Configuration class for Spring Security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter authFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for injecting required dependencies.
     *
     * @param authFilter        JWT authentication filter for securing requests.
     * @param userDetailsService Service to load user details.
     */
    public SecurityConfig(JwtAuthFilter authFilter, UserDetailsService userDetailsService) {
        this.authFilter = authFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures security filter chain, defining authorization, authentication, 
     * and session management policies.
     *
     * @param http The HttpSecurity instance to configure security policies.
     * @return Configured SecurityFilterChain.
     * @throws Exception If any security configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (use only if necessary)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(authorize -> authorize
                        // Public endpoints that do not require authentication
                        .requestMatchers("/user/new", "/doctor/confirm-account", "doctor/register", 
                                         "/user/login", "/user/authenticate", "/user/welcome", "login/doctor","/patient/**","/actuator/**")
                        .permitAll()

                        // Protected endpoints that require authentication
                        .requestMatchers("doctor/all", "/api/patients/slot/{slotId}", 
                                         "/api/patients/post","/api/patients/doctor/{userId}/date/{date}")
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session policy to stateless (JWT-based auth)
                .authenticationProvider(authenticationProvider()) // Use custom authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before authentication
                .build();
    }

    /**
     * Defines the authentication provider, using DAO-based authentication 
     * with user details service and password encoder.
     *
     * @return Configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Set user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Use bcrypt password encoder
        return authProvider;
    }

    /**
     * Defines the password encoder for securing passwords.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication manager with user details service and password encoder.
     *
     * @param http The HttpSecurity instance.
     * @return Configured AuthenticationManager.
     * @throws Exception If authentication manager configuration fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Configures CORS settings to allow cross-origin requests from all domains.
     *
     * @return Configured CorsConfigurationSource.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow all origins
        configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, authentication headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this configuration to all endpoints
        return source;
    }
}
