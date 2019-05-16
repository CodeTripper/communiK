package com.goniyo.notification.template;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Template")
@Data
public class TemplateRepoDto {
    private @Id
    String id;
    private String templateName;

}
