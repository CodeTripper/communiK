package com.goniyo.notification.email;

import com.goniyo.notification.repository.mongo.NotificationMessageDto;
import reactor.core.publisher.Flux;

public interface EmailService {
    String salaryCredited(EmailDto emailDto);

    String salaryAdvanceCredited(EmailDto emailDto);

    String promoMailToCustomers(EmailDto emailDto);

    Flux<NotificationMessageDto> getAllEmails();

}
