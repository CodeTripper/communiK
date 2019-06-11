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
package in.codetripper.communik.domain.email.providers;

import java.util.Properties;
import org.springframework.stereotype.Service;
import in.codetripper.communik.domain.email.EmailConfiguration;
import in.codetripper.communik.domain.email.EmailId;
import in.codetripper.communik.domain.email.SmtpEmailSender;
import in.codetripper.communik.domain.notification.NotificationMessage;
import in.codetripper.communik.domain.provider.Provider;
import in.codetripper.communik.domain.provider.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor()
public class Gmail extends SmtpEmailSender {

  private final ProviderService providerService;
  private String providerId = "11003";

  @Override
  protected EmailConfiguration getMailConfiguration() {
    Provider provider = providerService.getProvider(providerId);
    EmailConfiguration emailConfiguration = new EmailConfiguration();
    emailConfiguration.setHost(provider.getServer().getHost());
    emailConfiguration.setUsername(provider.getBasicAuthentication().getUserId());
    emailConfiguration.setPassword(provider.getBasicAuthentication().getPassword());
    emailConfiguration.setPort(provider.getServer().getPort());
    return emailConfiguration;

  }

  @Override
  protected Properties getMailProperties() {
    Provider provider = providerService.getProvider(providerId);
    Properties props = new Properties();
    props.put("mail.transport.protocol", provider.getServer().getProtocol());
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", provider.getServer().isTls());
    props.put("mail.debug", "false");
    return props;
  }

  @Override
  protected Properties preProcess(NotificationMessage<EmailId> email) {
    return null;
  }

  @Override
  protected Properties postProcess(NotificationMessage<EmailId> email) {
    return null;
  }


  @Override
  public boolean isPrimary() {
    return false;
  }
}
