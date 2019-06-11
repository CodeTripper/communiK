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
package in.codetripper.communik.domain.sms;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SmsController {

  private final SmsService smsService;

  @PostMapping(path = "/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Mono<NotificationStatusResponse> sms(@Valid @RequestBody SmsDto smsDto,
      ServerHttpRequest serverHttpRequest) {
    log.debug("Inside SMSController");
    String ipAddress = "";
    if (serverHttpRequest.getRemoteAddress() != null) {
      ipAddress = serverHttpRequest.getRemoteAddress().getAddress().getHostAddress();
    }
    smsDto.setIpAddress(ipAddress);
    return smsService.sendSms(smsDto);
  }

  @GetMapping(path = "/sms/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String getSmsStatus(@PathVariable String id) {
    NotificationStatusResponse notificationResponse = smsService.getSmsStatus(id);
    return "SUCCESS";
  }

  @PostMapping(path = "/smses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public final Flux<NotificationStatusResponse> smses(@Valid @RequestBody List<SmsDto> smsDtos) {
    log.debug("Received bulk sms request with data {}", smsDtos);
    return Flux.fromIterable(smsDtos).flatMap(smsService::sendSms);
  }
}
