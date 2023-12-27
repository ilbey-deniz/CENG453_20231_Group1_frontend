package com.group1.frontend.events;

import com.group1.frontend.utils.LobbyPlayer;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.TableCell;

public class PlayerKickedEvent extends Event {
    public static final EventType<PlayerKickedEvent> PLAYER_KICKED = new EventType<>(Event.ANY, "PLAYER_KICKED");
    private final TableCell<LobbyPlayer, LobbyPlayer> cell;

    public PlayerKickedEvent(TableCell<LobbyPlayer, LobbyPlayer> cell) {
        super(PLAYER_KICKED);
        this.cell = cell;
    }

    public TableCell<LobbyPlayer, LobbyPlayer> getTableCell() {
        return cell;
    }
}
