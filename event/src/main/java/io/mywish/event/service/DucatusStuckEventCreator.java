package io.mywish.event.service;

import io.mywish.event.model.BaseEvent;
import io.mywish.event.model.DucatusStuckEvent;
import org.springframework.stereotype.Component;

@Component
public class DucatusStuckEventCreator implements BaseEventCreator {
    @Override
    public BaseEvent createEvent(String message) {
        return new DucatusStuckEvent(message);
    }
}
