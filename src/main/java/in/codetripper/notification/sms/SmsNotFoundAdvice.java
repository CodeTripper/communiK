package in.codetripper.notification.sms;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class SmsNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(SmsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String smsNotFoundHandler(SmsNotFoundException ex) {
        return ex.getMessage();
    }
}
