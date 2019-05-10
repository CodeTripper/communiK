package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationResponse;

interface SmsService {
    String sendSms(SmsDTO smsDTO);

    NotificationResponse getSmsStatus(String id);
}
