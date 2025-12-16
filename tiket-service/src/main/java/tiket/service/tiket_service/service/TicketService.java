package tiket.service.tiket_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tiket.service.tiket_service.domain.dto.ticket.CreateTicketRequest;
import tiket.service.tiket_service.domain.dto.ticket.TicketResponse;
import tiket.service.tiket_service.domain.entity.EventEntity;
import tiket.service.tiket_service.domain.entity.TicketEntity;
import tiket.service.tiket_service.domain.enums.TicketStatus;
import tiket.service.tiket_service.domain.event.TicketReservedDomainEvent;
import tiket.service.tiket_service.domain.mapper.TicketMapper;
import tiket.service.tiket_service.exception.NoSeatsAvailableException;
import tiket.service.tiket_service.exception.NotFoundException;
import tiket.service.tiket_service.repository.EventRepository;
import tiket.service.tiket_service.repository.TicketRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final TicketMapper ticketMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public TicketResponse reserveTicket(CreateTicketRequest request) {
        log.info("Reserving ticket: {}", request);
        
        // check event availability
        EventEntity event = eventRepository.findByIdForUpdate(request.getEventId())
            .orElseThrow(() -> new NotFoundException("Event with id " + request.getEventId() + " not found"));

        // check if there are available seats
        if (event.getAvailableSeats() <= 0) {
            throw new NoSeatsAvailableException("No available seats for event " + event.getId());
        }

        // reserve seat
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        // create ticket
        TicketEntity ticket = ticketMapper.toEntity(request);
        ticket.setEvent(event);
        ticket.setStatus(TicketStatus.RESERVED);
        ticket.setReservedAt(Instant.now());
        ticket.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));

        // save ticket
        TicketEntity savedTicket = ticketRepository.save(ticket);

        applicationEventPublisher.publishEvent(
            new TicketReservedDomainEvent(
                savedTicket.getId(),
                event.getId(),
                request.getUserId(),
                event.getPrice(),
                savedTicket.getReservedAt()
            )
        );

        return ticketMapper.toResponse(savedTicket);
    }

    public TicketResponse getTicketById(Long id) {
        log.info("Getting ticket by id: {}", id);
        TicketEntity ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ticket with id " + id + " not found"));
        return ticketMapper.toResponse(ticket);
    }
}
