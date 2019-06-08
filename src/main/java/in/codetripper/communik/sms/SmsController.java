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
package in.codetripper.communik.sms;

import in.codetripper.communik.notification.NotificationStatusResponse;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SmsController {

  private final SmsService smsService;

  @PostMapping("/sms")
  public Mono<NotificationStatusResponse> sms(@Valid @RequestBody SmsDto smsDto,
      ServerHttpRequest serverHttpRequest) {
    log.debug("Inside SMSController");
    smsDto.setIpAddress(serverHttpRequest.getRemoteAddress().getAddress().getHostAddress());
    return smsService.sendSms(smsDto);
  }

  @GetMapping("/sms/{id}")
  public String getSmsStatus(@PathVariable String id) {
    NotificationStatusResponse notificationResponse = smsService.getSmsStatus(id);
    return "SUCCESS";
  }

  @RequestMapping(value = "/smses", method = RequestMethod.POST)
  public final Flux<NotificationStatusResponse> smses(@Valid @RequestBody List<SmsDto> smsDtos) {
    log.debug("Received bulk sms request with data {}", smsDtos);
    return Flux.fromIterable(smsDtos).flatMap(smsService::sendSms);
  }
}
