package com.goniyo.notification.template;

import com.goniyo.notification.notification.Type;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


@ToString
@Data
public class TemplateDto implements Serializable {
    private String id;
    private String name;
    private Type type;
    private String body;
    private boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String owner;
}
