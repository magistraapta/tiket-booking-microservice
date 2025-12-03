package tiket.booking.eventservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@AllArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        log.info("Creating event: {}", request.getName());

        EventEntity entity = eventMapper.toEntity(request);

        EventEntity savedEntity = eventRepository.save(entity);
        log.info("Event created with id: {}", savedEntity.getId());

        // Create tickets if provided
        if (request.getTickets() != null && !request.getTickets().isEmpty()) {
            log.info("Creating {} ticket types for event {}", request.getTickets().size(), savedEntity.getId());
            
            for (CreateTicketRequest ticketRequest : request.getTickets()) {
                int quantity = ticketRequest.getQuantity() != null ? ticketRequest.getQuantity() : 1;

                TicketEntity ticketEntity = eventMapper.toTicketEntity(ticketRequest);
                ticketEntity.setQuantity(quantity);

                // Generate unique ticket number for this ticket type
                String ticketNumber = generateTicketNumber(savedEntity.getId(), ticketRequest.getSeatNumber(), 0);
                ticketEntity.setTicketNumber(ticketNumber);

                // Convert String status to SeatStatus enum
                SeatStatus status = parseSeatStatus(ticketRequest.getStatus());
                ticketEntity.setStatus(status);

                // Add ticket to event (this will also persist it due to cascade)
                savedEntity.addTicket(ticketEntity);
            }
            
            // Save the event again to persist the tickets (cascade will handle it)
            savedEntity = eventRepository.save(savedEntity);
            log.info("Created {} tickets for event {}", 
                savedEntity.getTickets().size(), savedEntity.getId());
        }

        return eventMapper.toEventResponse(savedEntity);
    }

    private String generateTicketNumber(Long eventId, String seatNumber, int index) {
        
        String base = seatNumber != null && !seatNumber.isEmpty() 
            ? String.format("EVENT-%d-%s-%d", eventId, seatNumber, index)
            : String.format("EVENT-%d-TICKET-%d", eventId, index);
        
        return base + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private SeatStatus parseSeatStatus(String status) {
        if (status == null || status.isEmpty()) {
            return SeatStatus.AVAILABLE;
        }
        
        try {
            return SeatStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid seat status: {}. Defaulting to AVAILABLE", status);
            return SeatStatus.AVAILABLE;
        }
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId) {
        log.info("Getting event by id: {}", eventId);

        Optional<EventEntity> entity = eventRepository.findById(eventId);

        if (entity.isEmpty()) {
            throw new RuntimeException("Event not found");
        }

        return eventMapper.toEventResponse(entity.get());
    }

    @Transactional(readOnly = true)
    public List<ListEventResponse> getAllEvents() {
        log.info("Getting all events");

        List<EventEntity> entities = eventRepository.findAll();
        return entities.stream().map(eventMapper::toListEventResponse).collect(Collectors.toList());
    }

    @Transactional
    public EventResponse updateEvent(Long eventId, UpdateEventRequest request) {
        log.info("Updating event: {}", eventId);

        Optional<EventEntity> entity = eventRepository.findById(eventId);

        if (entity.isEmpty()) {
            throw new RuntimeException("Event not found");
        }

        if (request.getName() != null) {
            entity.get().setName(request.getName());
        }

        if (request.getDescription() != null) {
            entity.get().setDescription(request.getDescription());
        }
        
        if (request.getStartDate() != null) {
            entity.get().setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            entity.get().setEndDate(request.getEndDate());
        }
        
        
        if (request.getLocation() != null) {
            entity.get().setLocation(request.getLocation());
        }

        EventEntity savedEntity = eventRepository.save(entity.get());
        log.info("Event updated with id: {}", savedEntity.getId());
        return eventMapper.toEventResponse(savedEntity);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        log.info("Deleting event: {}", eventId);

        Optional<EventEntity> entity = eventRepository.findById(eventId);

        if (entity.isEmpty()) {
            throw new RuntimeException("Event not found");
        }

        eventRepository.deleteById(eventId);
        log.info("Event deleted with id: {}", eventId);
    }

}
