package in.codetripper.communik.sms.providers.twofactor;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import in.codetripper.communik.exceptions.NotificationSendFailedException;
import in.codetripper.communik.notification.NotificationStatusResponse;
import in.codetripper.communik.provider.ProviderService;
import in.codetripper.communik.sms.Sms;
import in.codetripper.communik.sms.SmsNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TwoFactorSmsNotifier implements SmsNotifier<Sms> {
    private ProviderService providerService;
    String providerId = "12002";
    @Override
    @HystrixCommand()
    public Mono<NotificationStatusResponse> send(Sms sms) throws NotificationSendFailedException {
        System.out.println("Inside Twofactor Notifier" + sms.toString());
        // ADD web flux webclient to call Gupchup
        return null;
    }

    @Override
    public boolean isDefault() {
        return providerService.getProvider(providerId).isPrimary();
    }

}
