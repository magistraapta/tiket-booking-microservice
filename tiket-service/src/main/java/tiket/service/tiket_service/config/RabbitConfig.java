package tiket.service.tiket_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String TICKET_EXCHANGE = "ticket.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    public static final String PAYMENT_QUEUE = "payment.ticket.queue";
    public static final String TICKET_QUEUE = "ticket.payment.queue";

    @Bean
    TopicExchange ticketExchange() {
        return new TopicExchange(TICKET_EXCHANGE);
    }

    @Bean
    TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    Queue ticketQueue() {
        return new Queue(TICKET_QUEUE, true);
    }

    @Bean
    Binding ticketReservedBinding() {
        return BindingBuilder
            .bind(paymentQueue())
            .to(ticketExchange())
            .with("ticket.reserved");
    }

    @Bean
    Binding paymentResultBinding() {
        return BindingBuilder
            .bind(ticketQueue())
            .to(paymentExchange())
            .with("payment.*");
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
