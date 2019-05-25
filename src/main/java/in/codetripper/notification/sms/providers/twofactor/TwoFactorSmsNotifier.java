package in.codetripper.notification.sms.providers.twofactor;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.notification.notification.NotificationFailedException;
import in.codetripper.notification.notification.NotificationStatusResponse;
import in.codetripper.notification.sms.Sms;
import in.codetripper.notification.sms.SmsNotifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("OTP")
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationFailedException {
        System.out.println("Inside Twofactor Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return null;
    }

    public String fallback(Sms sms) {
        System.out.println("Inside Twofactor FALLBACK  Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return "GUPCHUP";
    }


}
