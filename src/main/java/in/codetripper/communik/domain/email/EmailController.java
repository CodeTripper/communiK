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
package in.codetripper.communik.domain.email;


import java.util.List;
import javax.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

public class EmailController {

  CacheControl ccNoStore = CacheControl.noStore();
  private final EmailService emailService;

  @PostMapping(path = "/email", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public final Mono<NotificationStatusResponse> email(@Valid @RequestBody EmailDto emailDto,
      ServerHttpRequest serverHttpRequest) {
    String ipAddress = "";
    if (serverHttpRequest.getRemoteAddress() != null) {
      ipAddress = serverHttpRequest.getRemoteAddress().getAddress().getHostAddress();
    }
    emailDto.setIpAddress(ipAddress);
    log.debug("Received email request from  {}  with data {}", ipAddress, emailDto);
    return emailService.sendEmail(emailDto);
  }

  @PostMapping(path = "/emails", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public final Flux<NotificationStatusResponse> emails(
      @Valid @RequestBody List<EmailDto> emailDtos) {
    log.debug("Received bulk email request with data {}", emailDtos);
    return Flux.fromIterable(emailDtos).flatMap(emailService::sendEmail);
  }


}
