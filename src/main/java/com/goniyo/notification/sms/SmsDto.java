package com.goniyo.notification.sms;

import lombok.Data;

@Data
class SmsDto {
    private String to;
    private String message;
}
