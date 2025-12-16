package tiket.service.tiket_service.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tiket.service.tiket_service.domain.dto.event.CreateEventRequest;
import tiket.service.tiket_service.domain.dto.event.EventResponse;
import tiket.service.tiket_service.domain.entity.EventEntity;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(CreateEventRequest request);
    
    @Mapping(source = "price", target = "price")
    EventResponse toResponse(EventEntity entity);
}
