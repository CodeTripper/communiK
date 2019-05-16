package com.goniyo.notification.template;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;

    @PostMapping("template")
    Publisher<ResponseEntity<Template>> create(@RequestBody Template template) {
        return this.templateService.createTemplate(template)
                .map(p -> ResponseEntity.created(URI.create("/template/" + p.getId()))
                        .contentType(mediaType)
                        .build());
    }

    @GetMapping("template/{id}")
    Publisher<ResponseEntity<Template>> getTemplate(@PathVariable String templateId) {
        return null;
    }

    @GetMapping("templates")
    Publisher<ResponseEntity<Template>> getTemplates() {
        return null;
    }

    @PutMapping("template/{id}")
    Publisher<ResponseEntity<Template>> updateTemplate(@PathVariable String templateId, @RequestBody Template template) {
        return null;
    }

    @DeleteMapping("template/{id}")
    Publisher<ResponseEntity<Template>> deleteTemplate(@PathVariable String templateId) {
        return null;
    }
}
