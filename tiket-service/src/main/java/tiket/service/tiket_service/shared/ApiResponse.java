package tiket.service.tiket_service.shared;

import java.time.Instant;
import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private T data;
    private String message;
    private HttpStatus status;
    private Instant timestamp;

    public ApiResponse(T data, String message, HttpStatus status, Instant timestamp) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message, HttpStatus.OK, Instant.now());
    }

    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return new ApiResponse<>(null, message, status, Instant.now());
    }

}
