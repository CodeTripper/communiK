package in.codetripper.communik.notification;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String s) {
        super(s);
    }

    public InvalidRequestException(String s, Throwable e) {
        super(s, e);
    }

}
