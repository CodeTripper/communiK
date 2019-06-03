package in.codetripper.communik.email;


import in.codetripper.communik.notification.NotificationStatusResponse;
import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmailController {
    CacheControl ccNoStore = CacheControl.noStore();
    @Autowired
    private Tracer tracer;
    private final EmailService emailService;

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public final Mono<NotificationStatusResponse> email(@Valid @RequestBody EmailDto emailDto) {
        // no logic in controller. Just pickup DTOs and sendEmail to service
        log.debug("Received email request with data {}", emailDto);
        return emailService.sendEmail(emailDto);
    }


}

