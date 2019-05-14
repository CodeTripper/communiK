package com.goniyo.notification.email;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class EmailController {
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public final String emailSalary(@Valid @RequestBody EmailDto emailDto) {
        // no logic in controller. Just pickup DTOs and send to service
        log.debug("emaildto message{}", emailDto.getMessage());
        //return new ResponseEntity(customer, HttpStatus.OK);
        return emailService.salaryCredited(emailDto);
    }

    @RequestMapping(value = "/email/salary-advance", method = RequestMethod.POST)
    public final String emailSalaryAdvance(@Valid @RequestBody EmailDto emailDto) {
        // no logic in controller. Just pickup DTOs and send to service
        log.debug("emaildto message{}", emailDto.getMessage());
        //return new ResponseEntity(customer, HttpStatus.OK);
        return emailService.salaryAdvanceCredited(emailDto);
    }
}

