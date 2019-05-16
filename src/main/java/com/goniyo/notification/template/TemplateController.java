package com.goniyo.notification.template;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.net.URI;


@RestController
@Slf4j
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    @Autowired
    private TemplateMapper templateMapper;


    @PostMapping(value = "template", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<ResponseEntity<Template>> create(@RequestBody TemplateDto templateDto) {
        log.debug("template controler:{}", templateDto);
        Template template = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.create(template)
                .map(p -> ResponseEntity.created(URI.create("/template/" + p.getId()))
                        .build());
    }

    @GetMapping(value = "template/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Template> getTemplate(@NotBlank @PathVariable String id) {
        return templateService.get(id);
    }

    @GetMapping(value = "templates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Flux<Template> getTemplates() {
        return templateService.getAll();
    }

    @PutMapping(value = "template/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<ResponseEntity<Template>> updateTemplate(@NotBlank @PathVariable String id, @RequestBody TemplateDto templateDto) {
        templateDto.setId(id);
        Template template = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.update(template)
                .map(p -> ResponseEntity.created(URI.create("/template/" + p.getId()))
                        .build());
    }

    @DeleteMapping(value = "template/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Void> deleteTemplate(@NotBlank @PathVariable String id) {
        return templateService.delete(id);
    }
}
