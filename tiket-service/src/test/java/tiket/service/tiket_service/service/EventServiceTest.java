package tiket.service.tiket_service.service;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tiket.service.tiket_service.domain.dto.event.CreateEventRequest;
import tiket.service.tiket_service.domain.dto.event.EventResponse;
import tiket.service.tiket_service.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventServiceTest {
    @Autowired
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        CreateEventRequest request = CreateEventRequest.builder()
            .name("Test Event")
            .description("Test Description")
            .startDate(Instant.now())
            .price(100.0)
            .availableSeats(100)
            .totalSeats(100)
            .build();

        EventResponse response = eventService.createEvent(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Test Event", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(100, response.getAvailableSeats());
        assertEquals(100, response.getTotalSeats());
    }

    @Test
    public void testGetEventById() {
        // Create an event first
        CreateEventRequest request = CreateEventRequest.builder()
            .name("Test Event")
            .description("Test Description")
            .startDate(Instant.now())
            .price(100.0)
            .availableSeats(100)
            .totalSeats(100)
            .build();

        EventResponse createdEvent = eventService.createEvent(request);

        // Retrieve the event
        EventResponse retrievedEvent = eventService.getEventById(createdEvent.getId());

        assertNotNull(retrievedEvent);
        assertEquals(createdEvent.getId(), retrievedEvent.getId());
        assertEquals("Test Event", retrievedEvent.getName());
    }

    @Test
    public void testGetEventByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            eventService.getEventById(999L);
        });
    }
}
