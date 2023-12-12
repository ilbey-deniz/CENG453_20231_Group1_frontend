package com.group1.frontend.events;

import javafx.event.Event;
import javafx.event.EventType;

public class DiceRolledEvent extends Event {
    public static final EventType<DiceRolledEvent> DICE_ROLLED = new EventType<>(Event.ANY, "DICE_ROLLED");
    public static final EventType<DiceRolledEvent> DICE_ROLLED_ALREADY = new EventType<>(Event.ANY, "DICE_ROLLED_ALREADY");

    public DiceRolledEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
