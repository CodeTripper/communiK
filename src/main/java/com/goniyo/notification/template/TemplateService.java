package com.goniyo.notification.template;

import reactor.core.publisher.Mono;

public interface TemplateService {
    Mono<Template> createTemplate(Template template);
}
