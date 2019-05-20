package com.goniyo.notification.email.providers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SendGridResponse {
    private boolean status;
}
