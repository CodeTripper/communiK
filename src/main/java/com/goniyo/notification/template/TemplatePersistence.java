package com.goniyo.notification.template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplatePersistence {
    Mono<Template> create(Template template);

    Mono<Template> update(Template template);

    Flux<Template> getAll();

    Mono<Template> get(String id);

    Mono<Void> delete(String id);
}
