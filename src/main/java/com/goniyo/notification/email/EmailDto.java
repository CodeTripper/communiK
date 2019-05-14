package com.goniyo.notification.email;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmailDto implements Serializable {
    @Email(message = "Email should be valid")
    private String to;
    @NotBlank(message = "Message cannot be empty")
    private String message;
    private String subject;
    private LocalDateTime timestamp = LocalDateTime.now();

}
