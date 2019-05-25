package in.codetripper.communik.template;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TemplatePersistence {
    Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate);

    Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate);

    Flux<NotificationTemplate> getAll();

    Mono<NotificationTemplate> get(String id);

    Mono<Void> delete(String id);
}
