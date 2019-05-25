package in.codetripper.notification.webhooks;

import in.codetripper.notification.notification.Status;
import in.codetripper.notification.notification.Type;

import java.util.List;

public class WebhookClient {
    private String clientId;
    private String clientName;
    private List<Status> interests;
    private List<Type> notificationTypes;
    private String webhook;
    private String active;

}
