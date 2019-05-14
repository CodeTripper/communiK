package com.goniyo.notification.temp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class BaseClass {

    private String id;
    private @NonNull String name;
}
