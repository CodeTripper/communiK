package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationResponse;

interface SmsService {
    String sendSms(SmsDto smsDTO);

    NotificationResponse getSmsStatus(String id);
}
