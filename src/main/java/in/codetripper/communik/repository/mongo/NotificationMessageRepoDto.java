package in.codetripper.communik.repository.mongo;

import in.codetripper.communik.notification.NotificationMessage;
import in.codetripper.communik.notification.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "Notifications")
public class NotificationMessageRepoDto<T> {

    private @Id
    String id;
    private @NotNull List<String> to;
    private @NotNull NotificationMessage.Container body;
    private NotificationMessage.Container attachment;
    private NotificationMessage.Meta meta;
    private Status status;
    private LocalDateTime lastUpdated;
    private int attempts;
    private String subject;
    private String templateId;
    private List<NotificationMessage.BlackOut> blackouts;
    private List<NotificationMessage.Action<? extends NotificationMessage>> actions;
    public final int getAttempts() {
        return this.actions != null ? this.actions.size() : 0;
    }


}
