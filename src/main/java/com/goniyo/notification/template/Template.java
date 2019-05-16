package com.goniyo.notification.template;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@ToString
@Data
public class Template implements Serializable {
    private String id;
    private String templateName;
}
