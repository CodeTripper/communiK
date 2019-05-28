package in.codetripper.communik.sms;

import in.codetripper.communik.notification.NotificationService;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.notification.Type;
import reactor.core.publisher.Mono;

interface SmsService extends NotificationService {
    Mono<NotificationStatusResponse> sendSms(SmsDto smsDTO);

    Mono<NotificationStatusResponse> sendOtp(SmsDto smsDTO);

    NotificationStatusResponse getSmsStatus(String id);

    default Type getType() {
        return Type.SMS;
    }

}
