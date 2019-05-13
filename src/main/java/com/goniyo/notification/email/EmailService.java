package com.goniyo.notification.email;

public interface EmailService {
    String salaryCredited(EmailDto emailDto);

    String salaryAdvanceCredited(EmailDto emailDto);

    String promoMailToCustomers(EmailDto emailDto);

}
