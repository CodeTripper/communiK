package in.codetripper.communik.webhooks;

import in.codetripper.communik.notification.Status;
import in.codetripper.communik.notification.Type;

import java.util.List;

public class WebhookClient {
    private String clientId;
    private String clientName;
    private List<Status> interests;
    private List<Type> notificationTypes;
    private String webhook;
    private String active;

}
