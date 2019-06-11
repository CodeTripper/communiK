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
package in.codetripper.communik.domain.sms.providers.twofactor;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.communik.domain.notification.NotificationMessage;
import in.codetripper.communik.domain.notification.NotificationStatusResponse;
import in.codetripper.communik.domain.provider.ProviderService;
import in.codetripper.communik.domain.sms.SmsId;
import in.codetripper.communik.domain.sms.SmsNotifier;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TwoFactorSmsNotifier implements SmsNotifier<SmsId> {

  private ProviderService providerService;
  String providerId = "12002";

  @Override
  @HystrixCommand()
  public Mono<NotificationStatusResponse> send(NotificationMessage<SmsId> sms)
      throws NotificationSendFailedException {
    return null;
  }

  @Override
  public boolean isPrimary() {
    return false;
  }

}
