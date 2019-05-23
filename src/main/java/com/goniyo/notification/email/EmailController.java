package com.goniyo.notification.email;


import com.goniyo.notification.notification.NotificationStatusResponse;
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
public class EmailController {
    CacheControl ccNoStore = CacheControl.noStore();

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public final Mono<NotificationStatusResponse> emailSalary(@Valid @RequestBody EmailDto emailDto) {
        // no logic in controller. Just pickup DTOs and send to service
        log.debug("Received email request with data {}", emailDto);
        return emailService.send(emailDto);
    }

}

