package tiket.service.tiket_service.domain.dto.event;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEventRequest {
    private String name;
    private String description;
    private Instant startDate;
    private Integer availableSeats;
    private Integer totalSeats;
}
