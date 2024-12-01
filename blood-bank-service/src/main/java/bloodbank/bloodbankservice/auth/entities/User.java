package bloodbank.bloodbankservice.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is a required field and should not be blank.")
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password is a required field and should not be blank.")
    @Size(max = 120, message = "Password should not exceed 120 characters.")
    private String password;

    @NotBlank(message = "Email is a required field and should not be blank.")
    @Size(max = 50, message = "Email should not exceed 50 characters.")
    @Email(message = "Email that was provided is not of valid format.")
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
