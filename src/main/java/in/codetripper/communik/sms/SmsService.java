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

import in.codetripper.communik.notification.NotificationService;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.notification.Type;
import reactor.core.publisher.Mono;

interface SmsService extends NotificationService {

  Mono<NotificationStatusResponse> sendSms(SmsDto smsDTO);

  Mono<NotificationStatusResponse> sendOtp(SmsDto smsDTO);

  NotificationStatusResponse getSmsStatus(String id);

  default Type getType() {
    return Type.SMS;
  }

}
