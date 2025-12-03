package tiket.booking.eventservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tiket.booking.eventservice.domain.EventEntity;
import tiket.booking.eventservice.domain.TicketEntity;
import tiket.booking.eventservice.domain.dto.request.CreateEventRequest;
import tiket.booking.eventservice.domain.dto.request.CreateTicketRequest;
import tiket.booking.eventservice.domain.dto.request.UpdateEventRequest;
import tiket.booking.eventservice.domain.dto.response.EventResponse;
import tiket.booking.eventservice.domain.dto.response.ListEventResponse;
import tiket.booking.eventservice.domain.enums.SeatStatus;
import tiket.booking.eventservice.mapper.EventMapper;
import tiket.booking.eventservice.repository.EventRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("EventService Unit Tests")
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    private EventEntity eventEntity;
    private CreateEventRequest createEventRequest;
    private EventResponse eventResponse;
    private UpdateEventRequest updateEventRequest;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.now().plusDays(7);
        endDate = LocalDateTime.now().plusDays(8);

        eventEntity = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .description("Test Description")
                .startDate(startDate)
                .endDate(endDate)
                .location("Test Location")
                .tickets(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createEventRequest = CreateEventRequest.builder()
                .name("Test Event")
                .description("Test Description")
                .startDate(startDate)
                .endDate(endDate)
                .location("Test Location")
                .build();

        eventResponse = EventResponse.builder()
                .id(1L)
                .name("Test Event")
                .description("Test Description")
                .startDate(startDate)
                .endDate(endDate)
                .location("Test Location")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        updateEventRequest = new UpdateEventRequest();
    }

    @Test
    @DisplayName("Should create event successfully without tickets")
    void testCreateEvent_WithoutTickets() {
        // Given
        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(eventMapper.toEventResponse(eventEntity)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Event", result.getName());
        verify(eventMapper, times(1)).toEntity(createEventRequest);
        verify(eventRepository, times(1)).save(eventEntity);
        verify(eventMapper, times(1)).toEventResponse(eventEntity);
    }

    @Test
    @DisplayName("Should create event successfully with tickets")
    void testCreateEvent_WithTickets() {
        // Given
        CreateTicketRequest ticketRequest = new CreateTicketRequest();
        ticketRequest.setTicketType("VIP");
        ticketRequest.setPrice(new BigDecimal("100.00"));
        ticketRequest.setSeatNumber("A1");
        ticketRequest.setStatus("AVAILABLE");
        ticketRequest.setQuantity(2);

        createEventRequest.setTickets(Arrays.asList(ticketRequest));

        EventEntity savedEventWithTickets = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .tickets(new ArrayList<>())
                .build();

        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventWithTickets);
        when(eventMapper.toTicketEntity(any(CreateTicketRequest.class))).thenReturn(new TicketEntity());

        EventResponse responseWithTickets = EventResponse.builder()
                .id(1L)
                .name("Test Event")
                .build();
        when(eventMapper.toEventResponse(any(EventEntity.class))).thenReturn(responseWithTickets);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        verify(eventMapper, times(1)).toEntity(createEventRequest);
        verify(eventRepository, times(2)).save(any(EventEntity.class)); // Once for event, once for tickets
        verify(eventMapper, atLeastOnce()).toTicketEntity(any(CreateTicketRequest.class));
    }

    @Test
    @DisplayName("Should create event with tickets when quantity is null")
    void testCreateEvent_WithTickets_NullQuantity() {
        // Given
        CreateTicketRequest ticketRequest = new CreateTicketRequest();
        ticketRequest.setTicketType("VIP");
        ticketRequest.setPrice(new BigDecimal("100.00"));
        ticketRequest.setSeatNumber("A1");
        ticketRequest.setStatus("AVAILABLE");
        ticketRequest.setQuantity(null); // null quantity should default to 1

        createEventRequest.setTickets(Arrays.asList(ticketRequest));

        EventEntity savedEventWithTickets = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .tickets(new ArrayList<>())
                .build();

        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEventWithTickets);
        when(eventMapper.toTicketEntity(any(CreateTicketRequest.class))).thenReturn(new TicketEntity());

        EventResponse responseWithTickets = EventResponse.builder()
                .id(1L)
                .name("Test Event")
                .build();
        when(eventMapper.toEventResponse(any(EventEntity.class))).thenReturn(responseWithTickets);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        verify(eventRepository, times(2)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should get event by id successfully")
    void testGetEventById_Success() {
        // Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventMapper.toEventResponse(eventEntity)).thenReturn(eventResponse);

        // When
        EventResponse result = eventService.getEventById(eventId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Event", result.getName());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventMapper, times(1)).toEventResponse(eventEntity);
    }

    @Test
    @DisplayName("Should throw exception when event not found by id")
    void testGetEventById_NotFound() {
        // Given
        Long eventId = 999L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.getEventById(eventId);
        });

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventMapper, never()).toEventResponse(any());
    }

    @Test
    @DisplayName("Should get all events successfully")
    void testGetAllEvents_Success() {
        // Given
        EventEntity event1 = EventEntity.builder().id(1L).name("Event 1").build();
        EventEntity event2 = EventEntity.builder().id(2L).name("Event 2").build();
        List<EventEntity> entities = Arrays.asList(event1, event2);

        ListEventResponse response1 = ListEventResponse.builder().id(1L).name("Event 1").build();
        ListEventResponse response2 = ListEventResponse.builder().id(2L).name("Event 2").build();

        when(eventRepository.findAll()).thenReturn(entities);
        when(eventMapper.toListEventResponse(event1)).thenReturn(response1);
        when(eventMapper.toListEventResponse(event2)).thenReturn(response2);

        // When
        List<ListEventResponse> result = eventService.getAllEvents();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("Event 2", result.get(1).getName());
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(2)).toListEventResponse(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should return empty list when no events exist")
    void testGetAllEvents_EmptyList() {
        // Given
        when(eventRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<ListEventResponse> result = eventService.getAllEvents();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, never()).toListEventResponse(any());
    }

    @Test
    @DisplayName("Should update event successfully with all fields")
    void testUpdateEvent_Success_AllFields() {
        // Given
        Long eventId = 1L;
        updateEventRequest.setName("Updated Event");
        updateEventRequest.setDescription("Updated Description");
        updateEventRequest.setStartDate(startDate.plusDays(1));
        updateEventRequest.setEndDate(endDate.plusDays(1));
        updateEventRequest.setLocation("Updated Location");

        EventEntity updatedEntity = EventEntity.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .startDate(startDate.plusDays(1))
                .endDate(endDate.plusDays(1))
                .location("Updated Location")
                .build();

        EventResponse updatedResponse = EventResponse.builder()
                .id(1L)
                .name("Updated Event")
                .description("Updated Description")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(eventEntity)).thenReturn(updatedEntity);
        when(eventMapper.toEventResponse(updatedEntity)).thenReturn(updatedResponse);

        // When
        EventResponse result = eventService.updateEvent(eventId, updateEventRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(eventEntity);
        verify(eventMapper, times(1)).toEventResponse(updatedEntity);
    }

    @Test
    @DisplayName("Should update event successfully with partial fields")
    void testUpdateEvent_Success_PartialFields() {
        // Given
        Long eventId = 1L;
        updateEventRequest.setName("Updated Event");
        // Other fields are null

        EventEntity updatedEntity = EventEntity.builder()
                .id(1L)
                .name("Updated Event")
                .description("Test Description") // unchanged
                .build();

        EventResponse updatedResponse = EventResponse.builder()
                .id(1L)
                .name("Updated Event")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(eventEntity)).thenReturn(updatedEntity);
        when(eventMapper.toEventResponse(updatedEntity)).thenReturn(updatedResponse);

        // When
        EventResponse result = eventService.updateEvent(eventId, updateEventRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).save(eventEntity);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent event")
    void testUpdateEvent_NotFound() {
        // Given
        Long eventId = 999L;
        updateEventRequest.setName("Updated Event");
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.updateEvent(eventId, updateEventRequest);
        });

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, never()).save(any());
        verify(eventMapper, never()).toEventResponse(any());
    }

    @Test
    @DisplayName("Should delete event successfully")
    void testDeleteEvent_Success() {
        // Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(eventEntity));
        doNothing().when(eventRepository).deleteById(eventId);

        // When
        eventService.deleteEvent(eventId);

        // Then
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent event")
    void testDeleteEvent_NotFound() {
        // Given
        Long eventId = 999L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.deleteEvent(eventId);
        });

        assertEquals("Event not found", exception.getMessage());
        verify(eventRepository, times(1)).findById(eventId);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should parse seat status correctly")
    void testParseSeatStatus_ValidStatus() {
        // This tests the private parseSeatStatus method indirectly through createEvent
        CreateTicketRequest ticketRequest = new CreateTicketRequest();
        ticketRequest.setTicketType("VIP");
        ticketRequest.setPrice(new BigDecimal("100.00"));
        ticketRequest.setSeatNumber("A1");
        ticketRequest.setStatus("BOOKED"); // Test different status
        ticketRequest.setQuantity(1);

        createEventRequest.setTickets(Arrays.asList(ticketRequest));

        EventEntity savedEvent = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .tickets(new ArrayList<>())
                .build();

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setStatus(SeatStatus.BOOKED);

        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEvent);
        when(eventMapper.toTicketEntity(any(CreateTicketRequest.class))).thenReturn(ticketEntity);

        EventResponse response = EventResponse.builder().id(1L).build();
        when(eventMapper.toEventResponse(any(EventEntity.class))).thenReturn(response);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        verify(eventRepository, times(2)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should default to AVAILABLE when seat status is invalid")
    void testParseSeatStatus_InvalidStatus() {
        // This tests the private parseSeatStatus method with invalid status
        CreateTicketRequest ticketRequest = new CreateTicketRequest();
        ticketRequest.setTicketType("VIP");
        ticketRequest.setPrice(new BigDecimal("100.00"));
        ticketRequest.setSeatNumber("A1");
        ticketRequest.setStatus("INVALID_STATUS"); // Invalid status
        ticketRequest.setQuantity(1);

        createEventRequest.setTickets(Arrays.asList(ticketRequest));

        EventEntity savedEvent = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .tickets(new ArrayList<>())
                .build();

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setStatus(SeatStatus.AVAILABLE); // Should default to AVAILABLE

        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEvent);
        when(eventMapper.toTicketEntity(any(CreateTicketRequest.class))).thenReturn(ticketEntity);

        EventResponse response = EventResponse.builder().id(1L).build();
        when(eventMapper.toEventResponse(any(EventEntity.class))).thenReturn(response);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        verify(eventRepository, times(2)).save(any(EventEntity.class));
    }

    @Test
    @DisplayName("Should default to AVAILABLE when seat status is null")
    void testParseSeatStatus_NullStatus() {
        // This tests the private parseSeatStatus method with null status
        CreateTicketRequest ticketRequest = new CreateTicketRequest();
        ticketRequest.setTicketType("VIP");
        ticketRequest.setPrice(new BigDecimal("100.00"));
        ticketRequest.setSeatNumber("A1");
        ticketRequest.setStatus(null); // Null status
        ticketRequest.setQuantity(1);

        createEventRequest.setTickets(Arrays.asList(ticketRequest));

        EventEntity savedEvent = EventEntity.builder()
                .id(1L)
                .name("Test Event")
                .tickets(new ArrayList<>())
                .build();

        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setStatus(SeatStatus.AVAILABLE); // Should default to AVAILABLE

        when(eventMapper.toEntity(createEventRequest)).thenReturn(eventEntity);
        when(eventRepository.save(any(EventEntity.class))).thenReturn(savedEvent);
        when(eventMapper.toTicketEntity(any(CreateTicketRequest.class))).thenReturn(ticketEntity);

        EventResponse response = EventResponse.builder().id(1L).build();
        when(eventMapper.toEventResponse(any(EventEntity.class))).thenReturn(response);

        // When
        EventResponse result = eventService.createEvent(createEventRequest);

        // Then
        assertNotNull(result);
        verify(eventRepository, times(2)).save(any(EventEntity.class));
    }
}
