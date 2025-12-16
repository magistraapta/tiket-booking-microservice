package tiket.service.tiket_service.domain.event;

import java.time.Instant;

public record TicketReservedDomainEvent(
    Long ticketId,
    Long eventId,
    Long userId,
    Double price,
    Instant reservedAt
) {}
