package tiket.service.tiket_service.shared;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private Instant timestamp;

    public ErrorResponse(String message, HttpStatus status, Instant timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
}
