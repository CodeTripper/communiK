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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import in.codetripper.communik.domain.notification.NotificationMessage;
import in.codetripper.communik.domain.notification.Status;
import lombok.Data;

@Data
@Document(collection = "Notifications")
public class NotificationMessageRepoDto<T> implements Serializable {

  private @Id String id;
  private @NotNull List<T> to;
  private @NotNull NotificationMessage.Container body;
  private List<T> cc;
  private List<T> bcc;
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
