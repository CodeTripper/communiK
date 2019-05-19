package com.goniyo.notification.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/*
 All domain logic to reside here , no reference of DTos here.
 */
@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplatePersistence templatePersistence;

    @Override
    public Mono<Template> create(Template template) {
        template.setCreated(LocalDateTime.now());
        return templatePersistence.create(template);
    }

    @Override
    public Mono<Template> update(Template template) {
        template.setUpdated(LocalDateTime.now());
        template.setActive(true);
        return templatePersistence.update(template);
    }

    @Override
    public Flux<Template> getAll() {
        return templatePersistence.getAll();
    }

    @Override
    // TODO check active
    public Mono<Template> get(String id) {
        return templatePersistence.get(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return templatePersistence.delete(id);
    }

}
