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
package in.codetripper.communik.repository.mongo;

import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.Status;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Notifications")
public class NotificationMessageRepoDto<T> {

  private @Id
  String id;
  private @NotNull List<String> to;
  private @NotNull NotificationMessage.Container body;
  private NotificationMessage.Container attachment;
  private NotificationMessage.Meta meta;
  private Status status;
  private LocalDateTime lastUpdated;
  private int attempts;
  private String subject;
  private String templateId;
  private String mediaType;
  private List<NotificationMessage.BlackOut> blackouts;
  private List<NotificationMessage.Action<? extends NotificationMessage>> actions;

  public final int getAttempts() {
    return this.actions != null ? this.actions.size() : 0;
  }


}
