package com.group1.frontend.events;

import com.group1.frontend.utils.IntegerPair;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class DiceRolledEvent extends Event {
    public static final EventType<DiceRolledEvent> DICE_ROLLED = new EventType<>(Event.ANY, "DICE_ROLLED");

    public static final EventType<DiceRolledEvent> CPU_ROLLED_DICE = new EventType<>(Event.ANY, "CPU_ROLLED_DICE");
    private final IntegerPair diceRoll;

    public DiceRolledEvent(EventType<? extends Event> eventType, IntegerPair diceRoll) {
        super(eventType);
        this.diceRoll = diceRoll;
    }

}
