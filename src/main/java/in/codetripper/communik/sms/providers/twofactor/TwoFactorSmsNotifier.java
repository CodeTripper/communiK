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
package in.codetripper.communik.sms.providers.twofactor;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.provider.ProviderService;
import in.codetripper.communik.sms.Sms;
import in.codetripper.communik.sms.SmsNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {

  private ProviderService providerService;
  String providerId = "12002";

  @Override
  @HystrixCommand()
  public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationSendFailedException {
    System.out.println("Inside Twofactor Notifier" + sms.toString());
    // ADD web flux webclient to call Gupchup
    return null;
  }

  @Override
  public boolean isPrimary() {
    return false;
  }

}
