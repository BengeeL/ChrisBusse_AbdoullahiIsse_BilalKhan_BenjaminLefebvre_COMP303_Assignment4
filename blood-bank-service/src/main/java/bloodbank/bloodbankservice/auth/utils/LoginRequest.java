package bloodbank.bloodbankservice.auth.utils;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
