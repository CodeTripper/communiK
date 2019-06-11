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

import in.codetripper.communik.domain.notification.NotificationService;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import in.codetripper.communik.domain.notification.Type;
import in.codetripper.communik.repository.mongo.NotificationMessageRepoDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// create base service and move common methods up
public interface EmailService extends NotificationService {

  Mono<NotificationStatusResponse> sendEmail(EmailDto emailDto);

  Flux<NotificationMessageRepoDto> getAllEmails();

  default Type getType() {
    return Type.EMAIL;
  }

}
