package asia.izzi.member.domain.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
