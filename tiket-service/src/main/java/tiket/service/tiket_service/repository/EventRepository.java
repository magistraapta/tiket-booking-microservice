package tiket.service.tiket_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import tiket.service.tiket_service.domain.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> findByIdForUpdate(Long id);
}
