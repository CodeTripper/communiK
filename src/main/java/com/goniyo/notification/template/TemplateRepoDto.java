package com.goniyo.notification.template;

import com.goniyo.notification.notification.Type;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Template")
@Data
public class TemplateRepoDto {
    private @Id
    String id;
    private String name;
    private Type type;
    private String body;
    private boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String owner;

}
