package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationStatusResponse;

interface SmsService {
    String sendSms(SmsDto smsDTO);

    String sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);
}
