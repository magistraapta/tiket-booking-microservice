package tiket.service.tiket_service.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import lombok.RequiredArgsConstructor;
import tiket.service.tiket_service.config.RabbitConfig;
import tiket.service.tiket_service.domain.event.TicketReservedDomainEvent;

@Component
@RequiredArgsConstructor
public class TicketEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTicketReservedEvent(TicketReservedDomainEvent event) {
        rabbitTemplate.convertAndSend(RabbitConfig.TICKET_EXCHANGE, "ticket.reserved", event);
    }
}
