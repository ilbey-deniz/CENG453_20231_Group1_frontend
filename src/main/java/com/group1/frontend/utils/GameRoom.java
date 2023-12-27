package com.group1.frontend.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
public class GameRoom {
    private String roomCode;
    private HashSet<LobbyPlayer> players;

    public GameRoom(String roomCode) {
        this.roomCode = roomCode;
    }
}
