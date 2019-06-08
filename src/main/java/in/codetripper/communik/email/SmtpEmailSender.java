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

import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import reactor.core.publisher.Mono;

@Slf4j
/*
 * WARNING DO NOT USE. SMTP is blocking.This is only for test purpose.
 */
public abstract class SmtpEmailSender implements EmailNotifier<Email> {

  private final JavaMailSenderImpl sender;

  public SmtpEmailSender() {
    sender = new JavaMailSenderImpl();
    // props = sender.getJavaMailProperties();
  }

  @Override
  public final Mono<NotificationStatusResponse> send(Email email)
      throws NotificationSendFailedException {
    log.debug("starting email sender");
    try {
      preProcess(email);
      process(email);
      postProcess(email);
    } catch (MessagingException | MailException e) {
      throw new NotificationSendFailedException("Unable to sendEmail Email", e);
    }
    return null;
  }

  private boolean process(Email email) throws MailException, MessagingException {
    EmailConfiguration emailConfiguration = getMailConfiguration();
    sender.setHost(emailConfiguration.getHost());
    sender.setPassword(emailConfiguration.getPassword());
    sender.setUsername(emailConfiguration.getUsername());
    sender.setPort(emailConfiguration.getPort());
    sender.setProtocol(emailConfiguration.getProtocol());
    sender.setJavaMailProperties(getMailProperties());
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    List<String> list = email.getTo();
    helper.setTo(list.toArray(new String[0]));
    helper.setSubject(email.getSubject());
    helper.setText(email.getBodyTobeSent());

    if (email.getAttachment() != null) {
    /*  FileSystemResource file =
          new FileSystemResource(new File(email.getAttachment().getMessage()));*/
      //helper.addAttachment("CoolImage.jpg", null);
    }
    sender.send(message);
    return true;

  }

  protected abstract EmailConfiguration getMailConfiguration();

  protected abstract Properties getMailProperties();

  protected abstract Properties preProcess(Email email);

  protected abstract Properties postProcess(Email email);

}
