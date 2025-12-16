package tiket.service.tiket_service.service;

import java.time.Instant;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tiket.service.tiket_service.domain.dto.event.CreateEventRequest;
import tiket.service.tiket_service.domain.dto.event.EventResponse;
import tiket.service.tiket_service.domain.entity.EventEntity;
import tiket.service.tiket_service.domain.mapper.EventMapper;
import tiket.service.tiket_service.exception.NotFoundException;
import tiket.service.tiket_service.repository.EventRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventResponse createEvent(CreateEventRequest request) {
        log.info("Creating event: {}", request);
        EventEntity event = eventMapper.toEntity(request);

        event.setCreatedAt(Instant.now());
        event.setUpdatedAt(Instant.now());
        
        EventEntity savedEvent = eventRepository.save(event);
        return eventMapper.toResponse(savedEvent);
    }

    public EventResponse getEventById(Long id) {
        log.info("Getting event by id: {}", id);
        EventEntity event = eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event with id " + id + " not found"));
        return eventMapper.toResponse(event);
    }
    
}
