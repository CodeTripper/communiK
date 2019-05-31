package in.codetripper.communik.common;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CommunikFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).compose((call) -> filter(exchange, call));
    }

    private Publisher<Void> filter(ServerWebExchange exchange, Mono<Void> call) {
        long start = System.nanoTime();
        return call.doOnSuccess((done) -> onSuccess(exchange, start))
                .doOnError((cause) -> onError(exchange, start, cause));
    }

    private void onSuccess(ServerWebExchange exchange, long start) {
        record(exchange, start, null);
    }

    private void onError(ServerWebExchange exchange, long start, Throwable cause) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            record(exchange, start, cause);
        } else {
            response.beforeCommit(() -> {
                record(exchange, start, cause);
                return Mono.empty();
            });
        }
    }

    private void record(ServerWebExchange exchange, long start, Throwable cause) {
        log.debug("Time Taken to process in nanos{}", System.nanoTime() - start);
    }
}
