package in.codetripper.communik.sms;

import in.codetripper.communik.notification.NotificationMessage;
import lombok.Getter;

@Getter
public class Sms extends NotificationMessage {
    private long mobileNo;
    private long countryCode;

    // TODO change the below hack to Superbuilder when milestone 25 is released Idea plugin
    // https://github.com/mplushnikov/lombok-intellij-plugin/milestone/31
    /*@Builder
    public Sms(Type type, String message, String to, String senderIp, Status status, long mobileNo, long countryCode,String templateId) {
        super(type, message, to, senderIp, status,templateId);
        this.mobileNo = mobileNo;
        this.countryCode = countryCode;
    }*/
}
