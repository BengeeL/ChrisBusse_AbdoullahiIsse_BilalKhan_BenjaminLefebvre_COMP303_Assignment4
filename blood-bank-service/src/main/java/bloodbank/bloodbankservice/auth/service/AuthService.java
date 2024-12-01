package bloodbank.bloodbankservice.auth.service;

import bloodbank.bloodbankservice.auth.utils.RegisterRequest;
import bloodbank.bloodbankservice.auth.entities.Role;
import bloodbank.bloodbankservice.auth.entities.User;
import bloodbank.bloodbankservice.auth.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }
}
