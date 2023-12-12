package com.group1.frontend.events;

import javafx.event.Event;
import javafx.event.EventType;

public class TurnEndedEvent extends Event {
    public static final EventType<TurnEndedEvent> TURN_ENDED = new EventType<>(Event.ANY, "TURN_ENDED");
    public TurnEndedEvent(EventType<? extends Event> eventType) {
        super(TURN_ENDED);
    }
}
