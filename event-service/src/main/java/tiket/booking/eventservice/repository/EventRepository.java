package tiket.booking.eventservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tiket.booking.eventservice.domain.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
