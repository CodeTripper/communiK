package com.goniyo.notification.template;

import com.goniyo.notification.notification.Type;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@ToString
@Data
public class NotificationTemplate implements Serializable {
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
    private String attachment;
    private List<String> bcc;
    private List<String> cc;
    private String replyTo;


}

