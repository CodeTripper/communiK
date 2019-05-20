package com.goniyo.notification.sms;

import com.goniyo.notification.notification.NotificationStatusResponse;
import reactor.core.publisher.Mono;

interface SmsService {
    Mono<NotificationStatusResponse> sendSms(SmsDto smsDTO);

    Mono<NotificationStatusResponse> sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);
}
