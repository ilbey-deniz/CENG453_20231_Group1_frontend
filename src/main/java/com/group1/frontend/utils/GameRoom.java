package com.group1.frontend.utils;

import com.group1.frontend.enums.PlayerColor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class GameRoom {
    private String roomCode;
    private String host;
    private HashMap<String, LobbyPlayer> players;

    public GameRoom() {
        players = new HashMap<>();
    }
    public void addPlayer(LobbyPlayer player) {
        players.put(player.getUsername(), player);
    }
    public void setHost(String username) {
        this.host = username;
        players.get(username).setHost(true);
    }

    public PlayerColor getAvailableColor() {
        for (PlayerColor color : PlayerColor.values()) {
            if (!players.containsValue(color)) {
                return color;
            }
        }
        return null;
    }
}
