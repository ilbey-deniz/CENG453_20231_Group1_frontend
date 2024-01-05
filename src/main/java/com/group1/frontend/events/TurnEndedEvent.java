package com.group1.frontend.events;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class TurnEndedEvent extends Event {
    public static final EventType<TurnEndedEvent> TURN_ENDED = new EventType<>(Event.ANY, "TURN_ENDED");
    public static final EventType<TurnEndedEvent> CPU_TURN_ENDED = new EventType<>(Event.ANY, "CPU_TURN_ENDED");
    public TurnEndedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
