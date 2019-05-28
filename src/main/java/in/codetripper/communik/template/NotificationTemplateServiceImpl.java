package in.codetripper.communik.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/*
 All domain logic to reside here , no reference of DTos here.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationTemplateServiceImpl implements NotificationTemplateService {
    private final NotificationTemplatePersistence templatePersistence;

    // TODO add validation
    @Override
    public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
        notificationTemplate.setCreated(LocalDateTime.now());
        return templatePersistence.create(notificationTemplate);
    }

    @Override
    public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
        notificationTemplate.setUpdated(LocalDateTime.now());
        notificationTemplate.setActive(true);
        return templatePersistence.update(notificationTemplate);
    }

    @Override
    public Flux<NotificationTemplate> getAll() {
        return templatePersistence.getAll();
    }

    @Override
    // TODO cache
    public Mono<NotificationTemplate> get(String id) {
        log.debug("Getting template for id {}", id);
        return templatePersistence.get(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return templatePersistence.delete(id);
    }

}
