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
    @Autowired
    private TemplateMapper templateMapper;
    private static final String BASE_PATH = "template";

    @PostMapping(value = BASE_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<ResponseEntity<Template>> create(@RequestBody TemplateDto templateDto) {
        log.debug("template controler:{}", templateDto);
        Template template = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.create(template)
                .map(p -> ResponseEntity.created(URI.create(BASE_PATH + p.getId()))
                        .build());
    }

    @GetMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Template> getTemplate(@NotBlank @PathVariable String id) {
        return templateService.get(id);
    }

    @GetMapping(value = BASE_PATH + "s", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Flux<Template> getTemplates() {
        return templateService.getAll();
    }

    @PutMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Template> updateTemplate(@NotBlank @PathVariable String id, @RequestBody TemplateDto templateDto) {
        templateDto.setId(id);
        Template template = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.update(template);

    }

    @DeleteMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Void> deleteTemplate(@NotBlank @PathVariable String id) {
        return templateService.delete(id);
    }
}
