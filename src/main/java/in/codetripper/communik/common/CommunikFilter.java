/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package in.codetripper.communik.common;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Filter to optionally record timings
 *
 * @author CodeTripper
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class CommunikFilter implements WebFilter {

  @Autowired
  private final Tracer tracer;


  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return chain.filter(exchange).compose((call) -> filter(exchange, call));
  }

  private Publisher<Void> filter(ServerWebExchange exchange, Mono<Void> call) {
    long start = System.currentTimeMillis();
    return call.doOnSuccess(done -> onSuccess(exchange, start))
        .doOnError(cause -> onError(exchange, start, cause));
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
    log.info("processed {} in {} ms", exchange.getRequest().getPath(),
        System.currentTimeMillis() - start);
  }
}
