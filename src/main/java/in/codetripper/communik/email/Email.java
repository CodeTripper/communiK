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

import in.codetripper.communik.notification.NotificationMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Data
@NoArgsConstructor
@Slf4j
@ToString(callSuper = true)
public class Email extends NotificationMessage {

  private String subject;
  private String cc;
  private String bcc;
  private String replyTo;

  /*
   * @Builder public Email(Type type, String message, String to, String senderIp, Status status,
   * String subject, String attachment,String templateId) { super(type, message, to, senderIp,
   * status,templateId); this.subject = subject; this.attachment = attachment; }
   */
}
