package com.goniyo.notification.template;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@ToString
public class Template {
    private @Id
    String id;
    private String templateName;
}
