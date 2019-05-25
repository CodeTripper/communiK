package in.codetripper.notification.sms;

import in.codetripper.notification.notification.NotificationStatusResponse;
import reactor.core.publisher.Mono;

interface SmsService {
    Mono<NotificationStatusResponse> sendSms(SmsDto smsDTO);

    Mono<NotificationStatusResponse> sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);
}
