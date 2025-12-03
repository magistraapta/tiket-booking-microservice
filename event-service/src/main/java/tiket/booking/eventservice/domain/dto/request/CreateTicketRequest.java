package tiket.booking.eventservice.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateTicketRequest {

    @NotBlank(message = "ticket type is required")
    @Size(min = 1, max = 255)
    private String ticketType;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal price;

    @NotBlank
    @Size(max = 50, message = "Seat number cannot exceed 50 characters")
    private String seatNumber;

    @NotNull
    private String status = "available";

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 1000, message = "Quantity cannot exceed 1000")
    private Integer quantity = 1; // Default to 1

}
