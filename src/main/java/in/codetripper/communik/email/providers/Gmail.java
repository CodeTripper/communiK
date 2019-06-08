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
package in.codetripper.communik.email.providers;

import static in.codetripper.communik.email.Constants.GMAIL;

import in.codetripper.communik.email.Email;
import in.codetripper.communik.email.EmailConfiguration;
import in.codetripper.communik.email.SmtpEmailSender;
import in.codetripper.communik.provider.Provider;
import in.codetripper.communik.provider.ProviderService;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Qualifier(GMAIL)
@RequiredArgsConstructor()
public class Gmail extends SmtpEmailSender {

  private final ProviderService providerService;
  String providerId = "11003";

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
  protected Properties preProcess(Email email) {
    return null;
  }

  @Override
  protected Properties postProcess(Email email) {
    return null;
  }


  @Override
  public boolean isPrimary() {
    return false;
  }
}
