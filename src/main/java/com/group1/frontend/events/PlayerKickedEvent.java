package com.group1.frontend.events;

import com.group1.frontend.utils.LobbyPlayer;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.TableCell;
import lombok.Getter;

@Getter
public class PlayerKickedEvent extends Event {
    public static final EventType<PlayerKickedEvent> PLAYER_KICKED = new EventType<>(Event.ANY, "PLAYER_KICKED");
    private final LobbyPlayer player;

    public PlayerKickedEvent(LobbyPlayer player) {
        super(PLAYER_KICKED);
        this.player = player;
    }

}
