package com.group1.frontend.events;

import com.group1.frontend.components.Player;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class GameWonEvent extends Event {
    public Player winner = null;
    public static final EventType<GameWonEvent> GAME_WON = new EventType<>(Event.ANY, "GAME_WON");

    public GameWonEvent(Player winner) {
        super(GAME_WON);
        this.winner = winner;
    }

}
