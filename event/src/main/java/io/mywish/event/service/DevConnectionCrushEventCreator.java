package io.mywish.event.service;

import io.mywish.event.model.BaseEvent;
import io.mywish.event.model.DevConnectionCrushEvent;
import org.springframework.stereotype.Component;

@Component
public class DevConnectionCrushEventCreator implements BaseEventCreator{
    @Override
    public BaseEvent createEvent(String message) {
        return new DevConnectionCrushEvent(message);
    }
}
