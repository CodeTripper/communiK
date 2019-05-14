package com.goniyo.notification.webhooks;

import com.goniyo.notification.email.EmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Slf4j
public class WebhookController {
    // TODO register/deregister/ which which messages/events/
    @RequestMapping(value = "/webhook/ping", method = RequestMethod.POST)
    public final String emailSalary(@Valid @RequestBody EmailDto emailDto) {
        // no logic in controller. Just pickup DTOs and send to service
        log.debug("emaildto message{}", emailDto.getMessage());
        //return new ResponseEntity(customer, HttpStatus.OK);
        return "";
    }

}
