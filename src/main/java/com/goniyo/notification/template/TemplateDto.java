package com.goniyo.notification.template;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@ToString
@Data
public class TemplateDto implements Serializable {
    private String id;
    private String templateName;
}
