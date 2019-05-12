package com.goniyo.notification.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

public class EmailDto implements Serializable {
    @Email(message = "Email should be valid")
    private String to;
    @NotBlank(message = "Message cannot be empty")
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
