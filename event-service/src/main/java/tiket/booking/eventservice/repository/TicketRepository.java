package tiket.booking.eventservice.repository;

import org.springframework.data.repository.Repository;
import tiket.booking.eventservice.domain.TicketEntity;

import java.util.List;

public interface TicketRepository extends Repository<TicketEntity, Long> {
    List<TicketEntity> findByEventId(Long eventId);
    List<TicketEntity> findByTicketType(String tiketType);
}
