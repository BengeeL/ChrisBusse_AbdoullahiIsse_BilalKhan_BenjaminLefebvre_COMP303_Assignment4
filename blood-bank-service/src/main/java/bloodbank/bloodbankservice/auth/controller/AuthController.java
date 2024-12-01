package bloodbank.bloodbankservice.auth.controller;

import bloodbank.bloodbankservice.auth.service.AuthService;
import bloodbank.bloodbankservice.auth.service.JwtService;
import bloodbank.bloodbankservice.auth.utils.RegisterRequest;
import bloodbank.bloodbankservice.auth.utils.AuthenticationRequest;
import bloodbank.bloodbankservice.auth.utils.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(user);
            
            logger.info("Login successful for user: {}", user.getUsername());
            return ResponseEntity.status(200).body(new AuthenticationResponse(jwtToken));
        } catch (Exception e) {
            logger.error("Login error", e);
            return ResponseEntity.status(401).body(new AuthenticationResponse(null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.status(200).body("Registration successful");
        } catch (Exception e) {
            logger.error("Registration error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT tokens are stateless, so we don't need to do anything server-side
        return ResponseEntity.ok().body("Logged out successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid token format");
            }

            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);
            
            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    String newToken = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new AuthenticationResponse(newToken));
                }
            }
            return ResponseEntity.status(401).body("Invalid token");
        } catch (Exception e) {
            logger.error("Token refresh error", e);
            return ResponseEntity.status(401).body("Token refresh failed");
        }
    }
}
