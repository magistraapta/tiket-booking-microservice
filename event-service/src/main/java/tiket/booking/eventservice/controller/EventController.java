package tiket.booking.eventservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import tiket.booking.eventservice.domain.dto.request.CreateEventRequest;
import tiket.booking.eventservice.domain.dto.request.UpdateEventRequest;
import tiket.booking.eventservice.domain.dto.response.EventResponse;
import tiket.booking.eventservice.domain.dto.response.ListEventResponse;
import tiket.booking.eventservice.service.EventService;
import tiket.booking.eventservice.shared.ApiResponse;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    /*
    Getting event by id
    GET /event/{id}
    Response: EventResponse
    Status: 200 OK
    */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long id) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.success("Event retrieved successfully", response));
    }

    /*
    Getting all events
    GET /event
    Response: List<EventResponse>
    Status: 200 OK
    */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ListEventResponse>>> getAllEvents() {
        List<ListEventResponse> response = eventService.getAllEvents();
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.success("Events retrieved successfully", response));
    }

    /*
    Create new event
    POST /event
    Request: CreateEventRequest
    Response: EventResponse
    Status: 201 Created
    */
    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@RequestBody CreateEventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success("Event created successfully", response));
    }

    /*
    Updating event
    PUT /event/{id}
    Request: UpdateEventRequest
    Response: EventResponse
    Status: 200 OK
    */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(@PathVariable Long id, @RequestBody UpdateEventRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(ApiResponse.success("Event updated successfully", response));
    }

    /*
    Deleting event
    DELETE /event/{id}
    Response: void
    Status: 204 No Content  
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Event deleted successfully", null));
    }

}