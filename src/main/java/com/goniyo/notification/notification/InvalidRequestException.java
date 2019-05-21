package com.goniyo.notification.notification;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String s) {
        super(s);
    }

    public InvalidRequestException(String s, Exception e) {
        super(s, e);
    }

}
