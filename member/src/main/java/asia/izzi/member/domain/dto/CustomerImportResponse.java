package asia.izzi.member.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerImportResponse {
    private int total;
    private int success;
    private int error;
    private String fileName;
    private List<CustomerImport> listErrorImport;
}
