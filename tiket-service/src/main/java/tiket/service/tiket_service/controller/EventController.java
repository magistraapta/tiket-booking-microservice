package tiket.service.tiket_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import tiket.service.tiket_service.domain.dto.event.CreateEventRequest;
import tiket.service.tiket_service.domain.dto.event.EventResponse;
import tiket.service.tiket_service.service.EventService;
import tiket.service.tiket_service.shared.ApiResponse;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(ApiResponse.success(eventService.createEvent(request), "Event created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEventById(id), "Event found successfully"));
    }
}
