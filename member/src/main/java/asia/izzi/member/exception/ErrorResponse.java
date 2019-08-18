package asia.izzi.member.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse implements Serializable {
    private String status;
    private String timestamp;
    private String message;
    private String debugMessage;
    private List<DetailError> detailErrors;

    public ErrorResponse() {
        timestamp = LocalDateTime.now().toString();
    }


    public ErrorResponse(HttpStatus status, Throwable ex) {
        this();
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public void addDetailError(DetailError detailError) {
        if (detailErrors == null) {
            detailErrors = new ArrayList<>();
        }
        detailErrors.add(detailError);
    }

}