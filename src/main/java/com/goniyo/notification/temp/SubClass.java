package com.goniyo.notification.temp;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SubClass extends BaseClass {

    private String age;

    @Builder
    public SubClass(String id, String name) {
        super(id, name);
    }
}

