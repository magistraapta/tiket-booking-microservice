package tiket.service.tiket_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tiket.service.tiket_service.domain.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    
}
