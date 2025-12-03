package tiket.booking.eventservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tiket.booking.eventservice.domain.dto.request.CreateEventRequest;
import tiket.booking.eventservice.domain.dto.response.EventResponse;
import tiket.booking.eventservice.service.EventService;
import tiket.booking.eventservice.shared.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void create_shouldReturnOkResponse() {
        // Given
        CreateEventRequest request = new CreateEventRequest();
        request.setName("Test Event");
        request.setDescription("Test Description");
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusHours(1));
        request.setLocation("Test Location");

        // When
        when(eventService.createEvent(request)).thenReturn(new EventResponse());

        ResponseEntity<ApiResponse<EventResponse>> response = eventController.createEvent(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Event created successfully", response.getBody().getMessage());
    }
    
}
