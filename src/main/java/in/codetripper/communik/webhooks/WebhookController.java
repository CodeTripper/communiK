package in.codetripper.communik.webhooks;

import in.codetripper.communik.template.NotificationTemplate;
import in.codetripper.communik.template.NotificationTemplateDto;
import in.codetripper.communik.template.NotificationTemplateMapper;
import in.codetripper.communik.template.NotificationTemplateService;
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
public class WebhookController {
    @Autowired
    private NotificationTemplateService templateService;
    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    @Autowired
    private NotificationTemplateMapper templateMapper;
    private static final String BASE_PATH = "webhook";

    @PostMapping(value = BASE_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<ResponseEntity<NotificationTemplate>> create(@RequestBody NotificationTemplateDto templateDto) {
        log.debug("template controler:{}", templateDto);
        NotificationTemplate template = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.create(template)
                .map(p -> ResponseEntity.created(URI.create(BASE_PATH + "/" + p.getId()))
                        .build());
    }

    @GetMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<NotificationTemplate> getTemplate(@NotBlank @PathVariable String id) {
        return templateService.get(id);
    }

    @GetMapping(value = BASE_PATH + "s", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Flux<NotificationTemplate> getTemplates() {
        return templateService.getAll();
    }

    @PutMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<NotificationTemplate> updateTemplate(@NotBlank @PathVariable String id, @RequestBody NotificationTemplateDto templateDto) {
        templateDto.setId(id);
        NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.update(notificationTemplate);

    }

    @DeleteMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Mono<Void> deleteTemplate(@NotBlank @PathVariable String id) {
        return templateService.delete(id);
    }

    @PostMapping(value = BASE_PATH + "/ping", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Publisher<ResponseEntity<NotificationTemplate>> ping(@RequestBody NotificationTemplateDto templateDto) {
        log.debug("notification controler:{}", templateDto);
        NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
        return this.templateService.create(notificationTemplate)
                .map(p -> ResponseEntity.created(URI.create(BASE_PATH + "/" + p.getId()))
                        .build());
    }
}
