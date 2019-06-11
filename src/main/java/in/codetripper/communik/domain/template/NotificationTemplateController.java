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
package in.codetripper.communik.domain.template;

import java.net.URI;
import java.util.Arrays;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationTemplateController {

  private final NotificationTemplateService templateService;
  private final NotificationTemplateMapper templateMapper;
  private static final String BASE_PATH = "template";

  @PostMapping(path = BASE_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Publisher<ResponseEntity<NotificationTemplate>> create(
      @RequestBody NotificationTemplateDto templateDto) {
    log.debug("notificationTemplate controler:{}", templateDto);
    NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
    return this.templateService.create(notificationTemplate)
        .map(p -> ResponseEntity.created(URI.create(BASE_PATH + "/" + p.getId())).build());
  }

  @GetMapping(path = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Mono<NotificationTemplate> getTemplate(@NotBlank @PathVariable String id) {
    return templateService.get(id);
  }

  @GetMapping(path = BASE_PATH + "s", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Flux<NotificationTemplate> getTemplates() {
    return templateService.getAll();
  }

  @GetMapping(path = BASE_PATH + "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Flux<String> getTest() {
    return Flux.fromIterable(Arrays.asList("foo", "bar"));
  }

  @PutMapping(path = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Mono<NotificationTemplate> updateTemplate(@NotBlank @PathVariable String id,
      @RequestBody NotificationTemplateDto templateDto) {
    templateDto.setId(id);
    NotificationTemplate notificationTemplate = templateMapper.templateDtoToTemplate(templateDto);
    return this.templateService.update(notificationTemplate);

  }

  @DeleteMapping(path = BASE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Mono<Void> deleteTemplate(@NotBlank @PathVariable String id) {
    return templateService.delete(id);
  }
}
