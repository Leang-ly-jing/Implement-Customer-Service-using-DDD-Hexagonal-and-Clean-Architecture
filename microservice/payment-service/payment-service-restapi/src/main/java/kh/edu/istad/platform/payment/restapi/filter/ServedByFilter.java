package kh.edu.istad.platform.payment.restapi.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ServedByFilter implements WebFilter {

    @Value("${server.port:9393}")
    private String port;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().add("X-Served-By", "payment-service:" + port);
        return chain.filter(exchange);
    }
}
