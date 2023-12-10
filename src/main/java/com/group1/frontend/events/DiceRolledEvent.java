package com.group1.frontend.events;

import javafx.event.Event;
import javafx.event.EventType;

public class DiceRolledEvent extends Event {
    public static final EventType<DiceRolledEvent> DICE_ROLLED = new EventType<>(Event.ANY, "DICE_ROLLED");
    private int diceRoll;
    public DiceRolledEvent(int diceRoll) {
        super(DICE_ROLLED);
        this.diceRoll = diceRoll;
    }
    public DiceRolledEvent() {
        super(DICE_ROLLED);
    }
    public int getDiceRoll() {
        return diceRoll;
    }
}
