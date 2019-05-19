package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationMessage;
import com.goniyo.notification.notification.NotificationStatusResponse;
import reactor.core.publisher.Mono;

interface SmsService {
    Mono<NotificationMessage> sendSms(SmsDto smsDTO);

    Mono<NotificationMessage> sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);
}
