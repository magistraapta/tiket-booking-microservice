package tiket.booking.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tiket.booking.orderservice.domain.OrderEntity;
import tiket.booking.orderservice.domain.dto.response.OrderResponse;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    Optional<OrderResponse> getOrderById(Long id);
}
