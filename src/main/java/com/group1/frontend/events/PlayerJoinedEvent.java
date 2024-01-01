package com.group1.frontend.events;

import com.group1.frontend.utils.LobbyPlayer;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

@Getter
public class PlayerJoinedEvent extends Event {
    public static final EventType<PlayerJoinedEvent> PLAYER_JOINED = new EventType<>(Event.ANY, "PLAYER_JOINED");
    private final LobbyPlayer lobbyPlayer;

    public PlayerJoinedEvent(LobbyPlayer lobbyPlayer) {
        super(PLAYER_JOINED);
        this.lobbyPlayer = lobbyPlayer;
    }


}
