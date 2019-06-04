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
package in.codetripper.communik.email;


import in.codetripper.communik.notification.NotificationStatusResponse;
import io.opentracing.Tracer;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {

  CacheControl ccNoStore = CacheControl.noStore();
  @Autowired
  private Tracer tracer;
  private final EmailService emailService;

  @RequestMapping(value = "/email", method = RequestMethod.POST)
  public final Mono<NotificationStatusResponse> email(@Valid @RequestBody EmailDto emailDto) {
    // no logic in controller. Just pickup DTOs and sendEmail to service
    log.debug("Received email request with data {}", emailDto);
    return emailService.sendEmail(emailDto);
  }


}
