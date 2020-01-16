package io.mywish.event.model;

import lombok.Getter;

@Getter
public class DucatusStuckEvent extends BaseEvent {
    public DucatusStuckEvent(String message) {
        super(message);
    }
}
