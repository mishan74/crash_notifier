package io.mywish.event.service;

import io.mywish.event.model.BaseEvent;

public interface BaseEventCreator {
    BaseEvent createEvent(String message);
}
