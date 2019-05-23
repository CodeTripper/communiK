package com.goniyo.notification.notification;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
// TODO change the below hack to Superbuilder when milestone 25 is released Idea plugin
// https://github.com/mplushnikov/lombok-intellij-plugin/milestone/31


@Data
@ToString
@Slf4j
@NoArgsConstructor

public class NotificationMessage {
    private String id; // from DB
    private @NotNull String to;
    private String bodyTobeSent;
    private @NotNull Container body;
    private Container attachment;
    private Meta meta;
    private Status status;
    private Notifiers notifiers;
    private List<Action> actions;
    private List<BlackOut> blackouts;
    private LocalDateTime lastUpdated;
    private int attempts;
    private int deadLine;
    private String templateId;
    @Data
    @NoArgsConstructor
    public static class Container {
        private String message;
        Map<String, Object> data = new LinkedHashMap<>();
    }

    /*
        All meta data of the message to ne here. Immutable
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
    public static class Notifiers {
        private Notifier primary;
        private List<Notifier> backup;
    }

    @Data
    public static class Action {
        private Notifier notifier;
        private String requestId;
        private String responseId;
        private @NotNull LocalDateTime started;
        private @NotNull LocalDateTime ended;
        private @NotNull LocalDateTime callbackAt;
        private boolean status;
    }

    /*
        Should be populated from category, but should be overridable
     */
    @Data
    public static class BlackOut {
        // by category
        private Type type;
        private LocalTime start;
        private LocalTime end;
    }


    /*public NotificationMessage(Type type, String message, String to, String senderIp, Status status, String templateId) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.message = message;
        this.to = to;
        this.senderIp = senderIp;
        this.status = status;
        this.created = LocalDateTime.now();
        this.templateId = templateId;
    }*/

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
