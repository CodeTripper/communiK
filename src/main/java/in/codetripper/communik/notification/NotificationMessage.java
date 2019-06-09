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
package in.codetripper.communik.notification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
// TODO change the below hack to Superbuilder when milestone 25 is released Idea plugin
// https://github.com/mplushnikov/lombok-intellij-plugin/milestone/31

/**
 * Base class to be used to send messages to the notifier providers
 *
 * @author CodeTripper
 */

@Data
@ToString
@Slf4j
@NoArgsConstructor

public class NotificationMessage<T> implements Serializable {

  private String id; // from DB
  private List<String> to;
  private String bodyTobeSent;

  private @NotNull Container body;
  // TODO should be a list of attachment
  private Attachment attachment;
  private Meta meta;
  private Status status;
  private Notifiers<? extends NotificationMessage> notifiers;
  private List<NotificationMessage.Action> actions;
  private List<NotificationMessage.BlackOut> blackouts;
  private LocalDateTime lastUpdated;
  private int attempts;
  private int deadLine;
  private String templateId;
  private String locale;
  private String from;
  private String mediaType;


  @Data
  @NoArgsConstructor
  public static class Container {

    private String message;
    Map<String, Object> data = new LinkedHashMap<>();
  }

  @Data
  @NoArgsConstructor
  public static class Attachment {

    private String mediaType;
    private byte[] content;
    private String name;
    private String placement;
  }
  /*
   * All meta data of the message to ne here. Immutable
   */
  @Data
  public static class Meta {

    private Type type;
    private String senderIp;
    private String category;
    private String lob;
    private int maxRetry; // from template
    private @NotNull LocalDateTime created; // by Timestam
    private @NotNull LocalDateTime expireBy;

  }

  @Data
  public static class Notifiers<K extends NotificationMessage> {

    private Notifier<K> primary;
    private List<? extends Notifier<K>> backup;
  }

  @Data
  public static class Action<K extends NotificationMessage> {

    private String notifier;
    private String requestId;
    private String responseId;
    private @NotNull LocalDateTime started;
    private @NotNull LocalDateTime ended;
    private @NotNull LocalDateTime callbackAt;
    private boolean status;
  }

  /*
   * Should be populated from category, but should be overridable
   */
  @Data
  public static class BlackOut {

    // by category
    private Type type;
    private LocalTime start;
    private LocalTime end;
  }


  /*
   * public NotificationMessage(Type type, String message, String to, String senderIp, Status
   * status, String templateId) { this.id = UUID.randomUUID().toString(); this.type = type;
   * this.message = message; this.to = to; this.senderIp = senderIp; this.status = status;
   * this.created = LocalDateTime.now(); this.templateId = templateId; }
   */

  public final void setStatus(Status status) {
    this.status = status;
  }

  public final Status getStatus() {
    return this.status;
  }

  public final int getAttempts() {
    return this.actions != null ? this.actions.size() : 0;
  }
}
