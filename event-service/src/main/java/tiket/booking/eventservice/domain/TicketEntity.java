package tiket.booking.eventservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tiket.booking.eventservice.domain.enums.SeatStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ticket_number", unique = true, nullable = false)
    private String ticketNumber;
    @Column(name = "ticket_type")
    private String ticketType;

    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "quantity")
    private Integer quantity;

    // Many-to-One relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    private EventEntity event;
}
