package com.goniyo.notification.webhooks;

import java.util.Observable;
import java.util.Observer;

public class WehhookHandler implements Observer {
    @Override
    public void update(Observable o, Object status) {
        // TODO List of webhook listeners, stored as well as runtime
        System.out.println("Webhook: " + status);
    }
}
