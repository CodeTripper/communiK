/*
 * Copyright 2019 CodeTripper
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package in.codetripper.communik.webhooks;

import java.net.URI;
import javax.validation.constraints.NotBlank;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import in.codetripper.communik.domain.template.NotificationTemplate;
import in.codetripper.communik.domain.template.NotificationTemplateDto;
import in.codetripper.communik.domain.template.NotificationTemplateMapper;
import in.codetripper.communik.domain.template.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequiredArgsConstructor
public class WebhookController {

  private final NotificationTemplateService templateService;
  private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
  private final NotificationTemplateMapper templateMapper;
  private static final String BASE_PATH = "webhook";

  @PostMapping(value = BASE_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  Publisher<ResponseEntity<NotificationTemplate>> create(
      @RequestBody NotificationTemplateDto templateDto) {
    log.debug("template controler:{}", templateDto);
    NotificationTemplate template = templateMapper.templateDtoToTemplate(templateDto);
    return this.templateService.create(template)
        .map(p -> ResponseEntity.created(URI.create(BASE_PATH + "/" + p.getId())).build());
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
  Mono<NotificationTemplate> updateTemplate(@NotBlank @PathVariable String id,
      @RequestBody NotificationTemplateDto templateDto) {
    templateDto.setId(id);
    NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
    return this.templateService.update(notificationTemplate);

  }

  @DeleteMapping(value = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  Mono<Void> deleteTemplate(@NotBlank @PathVariable String id) {
    return templateService.delete(id);
  }

  @PostMapping(value = BASE_PATH + "/ping", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  Publisher<ResponseEntity<NotificationTemplate>> ping(
      @RequestBody NotificationTemplateDto templateDto) {
    log.debug("notification controler:{}", templateDto);
    NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
    return this.templateService.create(notificationTemplate)
        .map(p -> ResponseEntity.created(URI.create(BASE_PATH + "/" + p.getId())).build());
  }
}
