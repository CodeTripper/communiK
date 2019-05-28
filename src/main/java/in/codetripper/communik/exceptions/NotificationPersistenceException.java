package in.codetripper.communik.exceptions;

public class NotificationPersistenceException extends RuntimeException {
    public NotificationPersistenceException(String s) {
        super(s);
    }

    public NotificationPersistenceException(String s, Throwable e) {
        super(s, e);
    }

}
