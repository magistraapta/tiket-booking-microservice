package tiket.service.tiket_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tiket.service.tiket_service.domain.dto.ticket.CreateTicketRequest;
import tiket.service.tiket_service.domain.dto.ticket.TicketResponse;
import tiket.service.tiket_service.domain.entity.EventEntity;
import tiket.service.tiket_service.domain.enums.TicketStatus;
import tiket.service.tiket_service.repository.EventRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testReserveTicket() {
        Instant now = Instant.now();
        EventEntity event = EventEntity.builder()
            .name("Test Event")
            .description("Test Description")
            .price(100.0)
            .startDate(now)
            .availableSeats(100)
            .totalSeats(100)
            .createdAt(now)
            .updatedAt(now)
            .build();
        EventEntity savedEvent = eventRepository.save(event);

        CreateTicketRequest request = CreateTicketRequest.builder()
            .eventId(savedEvent.getId())
            .userId(1L)
            .build();

        TicketResponse response = ticketService.reserveTicket(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(TicketStatus.RESERVED.name(), response.getStatus());
        assertEquals(savedEvent.getId(), response.getEventId());
        assertEquals(1L, response.getUserId());
    }
    
}
