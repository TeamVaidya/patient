package com.spring.vaidya.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Filter that runs once per request.
 * It validates JWT tokens and sets authentication in the Security Context if valid.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtAuthFilter.
     *
     * @param jwtUtils           Utility class for JWT operations.
     * @param userDetailsService Service to fetch user details.
     */
    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests and validates the JWT token if present.
     *
     * @param request  The HTTP request
     * @param response The HTTP response
     * @param chain    The filter chain
     * @throws ServletException If a servlet error occurs
     * @throws IOException      If an input-output error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            // Extract Authorization header
            String authHeader = request.getHeader("Authorization");

            // Check if Authorization header contains a Bearer token
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7); // Extract token after "Bearer "
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                // If username is found and user is not already authenticated
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    // Validate JWT token
                    if (jwtUtils.validateJwtToken(jwt)) {
                        // Create authentication token
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Set authentication in security context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            // Continue the filter chain
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "JWT token has expired", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            sendErrorResponse(response, "Invalid JWT token", HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            sendErrorResponse(response, "Could not process JWT token", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends a JSON error response when JWT processing fails.
     *
     * @param response  The HTTP response
     * @param message   The error message
     * @param statusCode The HTTP status code
     * @throws IOException If an input-output error occurs
     */
    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setContentType("application/json");
        response.setStatus(statusCode);

        // Construct JSON error response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("status", statusCode);
        errorResponse.put("timestamp", System.currentTimeMillis());

        // Convert map to JSON and write to response
        response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(errorResponse));
    }
}
