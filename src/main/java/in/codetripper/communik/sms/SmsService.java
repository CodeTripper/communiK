package in.codetripper.communik.sms;

import in.codetripper.communik.notification.NotificationStatusResponse;
import reactor.core.publisher.Mono;

interface SmsService {
    Mono<NotificationStatusResponse> sendSms(SmsDto smsDTO);

    Mono<NotificationStatusResponse> sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);
}
