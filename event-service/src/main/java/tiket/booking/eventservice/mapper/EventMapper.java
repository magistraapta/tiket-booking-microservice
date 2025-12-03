package tiket.booking.eventservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tiket.booking.eventservice.domain.EventEntity;
import tiket.booking.eventservice.domain.TicketEntity;
import tiket.booking.eventservice.domain.dto.request.CreateEventRequest;
import tiket.booking.eventservice.domain.dto.request.CreateTicketRequest;
import tiket.booking.eventservice.domain.dto.request.UpdateEventRequest;
import tiket.booking.eventservice.domain.dto.response.EventResponse;
import tiket.booking.eventservice.domain.dto.response.ListEventResponse;
import tiket.booking.eventservice.domain.dto.response.TicketResponse;

/*
Mapping event entity to create event request
Mapping ticket entity to create ticket request
Mapping event entity to update event request
Mapping event entity to event response
**/

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(CreateEventRequest createEventRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "ticketNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    TicketEntity toTicketEntity(CreateTicketRequest createTicketRequest);

    // Map field name differences: startTime -> startDate, endTime -> endDate
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget EventEntity eventEntity, UpdateEventRequest updateEventRequest);

    // Map field name differences: startDate -> startTime, endDate -> endTime
    EventResponse toEventResponse(EventEntity eventEntity);

    // Map TicketEntity to TicketResponse
    // Map nested fields: event.id -> eventId, event.name -> eventName
    // MapStruct automatically converts enum (SeatStatus) to String
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventName", source = "event.name")
    @Mapping(target = "quantity", source = "quantity")
    TicketResponse toTicketResponse(TicketEntity ticketEntity);

    ListEventResponse toListEventResponse(EventEntity eventEntity);
}
