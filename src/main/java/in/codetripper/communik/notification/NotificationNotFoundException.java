package in.codetripper.communik.notification;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException() {

    }

    public NotificationNotFoundException(String s) {
        // Call constructor of parent Exception
        super(s);
    }

    public NotificationNotFoundException(String s, Throwable e) {
        // Call constructor of parent Exception
        super(s, e);
    }

}
