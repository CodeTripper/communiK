package com.goniyo.notification.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public Mono<Template> createTemplate(Template template) {
        return this.templateRepository
                .save(template)
                .doOnSuccess(profile -> this.publisher.publishEvent(new NotificationEvent<Template>(profile, true)));
    }

}
