package in.codetripper.communik.template.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplatePersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Repository
@ConditionalOnProperty(value = "notification.template.location", havingValue = "filesystem")
@RequiredArgsConstructor
@Slf4j
public class FileSystemTemplateAdapter implements NotificationTemplatePersistence {
    //private final FileResource resource;
    private final ResourceLoader resourceLoader;

    @Value("${notification.template.location.filesystem:classpath:templates}")
    private String templatePath;


    @Override
    public Mono<NotificationTemplate> create(NotificationTemplate notificationTemplate) {
        return null;
    }

    @Override
    public Mono<NotificationTemplate> update(NotificationTemplate notificationTemplate) {
        return null;
    }

    @Override
    public Flux<NotificationTemplate> getAll() {
        return null;
    }


    @Override
    public Mono<NotificationTemplate> get(String id) {
        Resource resource = resourceLoader.getResource(templatePath + "/" + id + ".json");
        log.debug("template resource {}", id);
        NotificationTemplate notificationTemplate = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            notificationTemplate = objectMapper.readValue(resource.getFile(), NotificationTemplate.class);
        } catch (IOException e) {
            return Mono.error(e);
        }
        log.debug("returning template {}", notificationTemplate);
        return Mono.justOrEmpty(notificationTemplate);
    }

    @Override
    public Mono<Void> delete(String id) {
        return null;
    }
}
