package asia.izzi.member.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created on 12/22/2018
 */
@Data
@NoArgsConstructor
public class FieldInvalidError extends DetailError {
    private String objectName;
    private String field;
    private String errorMessage;

    public FieldInvalidError(String objectName, String field, String errorMessage) {
        this.objectName = objectName;
        this.field = field;
        this.errorMessage = errorMessage;
    }
}
