package in.codetripper.communik.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String s) {
        super(s);
    }

    public InvalidRequestException(String s, Throwable e) {
        super(s, e);
    }

}
