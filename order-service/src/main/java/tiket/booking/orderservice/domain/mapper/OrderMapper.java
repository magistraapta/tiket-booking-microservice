package tiket.booking.orderservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tiket.booking.orderservice.domain.OrderEntity;
import tiket.booking.orderservice.domain.dto.request.CreateOrderRequest;
import tiket.booking.orderservice.domain.dto.response.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id",  ignore = true)
    OrderEntity toOrderEntity(CreateOrderRequest order);

    OrderResponse toResponse(OrderEntity order);


}
