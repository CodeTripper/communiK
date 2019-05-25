package in.codetripper.communik.notification;

public class NotificationFailedException extends RuntimeException {
    public NotificationFailedException(String s) {
        // Call constructor of parent Exception
        super(s);
    }

    public NotificationFailedException(String s, Throwable e) {
        // Call constructor of parent Exception
        super(s, e);
    }

}