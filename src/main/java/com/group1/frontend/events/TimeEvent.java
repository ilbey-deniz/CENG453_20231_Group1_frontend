package com.group1.frontend.events;


import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class TimeEvent extends Event {
    public static final EventType<TimeEvent> ONE_TICK = new EventType<>(Event.ANY, "ONE_TICK");
    public static final EventType<TimeEvent> TIMES_UP = new EventType<>(Event.ANY, "TIMES_UP");
    private int remainingSeconds;
    public TimeEvent(int remainingSeconds) {
        super(ONE_TICK);
        this.remainingSeconds = remainingSeconds;
    }
    public TimeEvent() {
        super(TIMES_UP);
    }

}
