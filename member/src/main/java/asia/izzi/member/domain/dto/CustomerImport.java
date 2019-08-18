package asia.izzi.member.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerImport {
    private String name;
    private String phone;
    private String email;
    private String type;
    private String error;
    private String[] categoryNames;
}
