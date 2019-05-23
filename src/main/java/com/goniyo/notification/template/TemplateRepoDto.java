package com.goniyo.notification.template;

import com.goniyo.notification.notification.Type;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "NotificationTemplate")
@Data
public class TemplateRepoDto {
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
