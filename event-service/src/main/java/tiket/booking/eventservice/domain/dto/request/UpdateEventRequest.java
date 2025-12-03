package tiket.booking.eventservice.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {
    @Size(min = 3, max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Size(max = 300)
    private String location;
}
