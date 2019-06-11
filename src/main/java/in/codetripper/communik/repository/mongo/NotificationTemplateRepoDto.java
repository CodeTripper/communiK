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
import org.springframework.data.mongodb.core.mapping.Document;
import in.codetripper.communik.domain.notification.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "NotificationTemplate")
@Data
public class NotificationTemplateRepoDto<T> implements Serializable {

  private String id;
  private String name;
  private String category;
  private String lob;
  private Type type;
  private boolean active;
  private LocalDateTime created;
  private LocalDateTime updated;
  private String owner;
  private String body;
  private List<Container> attachments;
  private List<T> bcc;
  private List<T> cc;
  private T replyTo;
  private String mediaType;

  @Data
  @NoArgsConstructor
  public static class Container implements Serializable {

    private String mediaType;
    private String method;
    private String source;
    private String name;
    private String placement;
  }

}
