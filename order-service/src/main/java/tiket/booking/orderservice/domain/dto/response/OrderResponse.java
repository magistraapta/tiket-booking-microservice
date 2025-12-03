package tiket.booking.orderservice.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Long eventId;
    private String ticketType;
    private String status;
    private float totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
}
