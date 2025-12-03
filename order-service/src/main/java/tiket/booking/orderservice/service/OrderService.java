package tiket.booking.orderservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tiket.booking.orderservice.domain.OrderEntity;
import tiket.booking.orderservice.domain.dto.request.CreateOrderRequest;
import tiket.booking.orderservice.domain.dto.response.OrderResponse;
import tiket.booking.orderservice.domain.mapper.OrderMapper;
import tiket.booking.orderservice.exception.ResourceNotFoundException;
import tiket.booking.orderservice.repository.OrderRepository;


@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    // Create
    public OrderResponse createOrder(CreateOrderRequest order) {
        OrderEntity orderEntity = mapper.toOrderEntity(order);
        OrderResponse orderResponse = mapper.toResponse(orderRepository.save(orderEntity));
        return orderResponse;
    }

    // READ
    public OrderResponse getOrderById(Long id) {
        log.info("Getting order by id: {}", id);

        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return  mapper.toResponse(orderEntity);
    }


}

