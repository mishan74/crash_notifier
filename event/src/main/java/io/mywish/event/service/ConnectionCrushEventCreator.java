package io.mywish.event.service;

import io.mywish.event.model.BaseEvent;
import io.mywish.event.model.ConnectionCrushEvent;
import org.springframework.stereotype.Component;

@Component
public class ConnectionCrushEventCreator implements BaseEventCreator {
    @Override
    public BaseEvent createEvent(String message) {
        return new ConnectionCrushEvent(message);
    }
}
