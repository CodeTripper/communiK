package in.codetripper.communik.email;

import in.codetripper.communik.notification.NotificationMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Data
@NoArgsConstructor
@Slf4j
@ToString(callSuper = true)
public class Email extends NotificationMessage {
    private String subject;
    private String cc;
    private String bcc;
    private String reply_to; // TODO

/*    @Builder
    public Email(Type type, String message, String to, String senderIp, Status status, String subject, String attachment,String templateId) {
        super(type, message, to, senderIp, status,templateId);
        this.subject = subject;
        this.attachment = attachment;
    }*/
}