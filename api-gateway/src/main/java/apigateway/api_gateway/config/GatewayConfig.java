package apigateway.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("event-service", r -> r
                .path("/event/**")
                .uri("lb://event-service"))
            .route("order-service", r -> r
                .path("/order/**")
                .uri("lb://order-service"))
            .build();
    }
}
