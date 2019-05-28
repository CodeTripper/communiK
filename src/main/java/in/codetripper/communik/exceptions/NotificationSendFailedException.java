package in.codetripper.communik.exceptions;

public class NotificationSendFailedException extends RuntimeException {
    public NotificationSendFailedException(String s) {
        super(s);
    }

    public NotificationSendFailedException(String s, Throwable e) {
        // Call constructor of parent Exception
        super(s, e);
    }

}
