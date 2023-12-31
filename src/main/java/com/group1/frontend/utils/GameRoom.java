package com.group1.frontend.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.enums.PlayerColor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class GameRoom {
    private String roomCode;
    private String hostName;
    @JsonDeserialize(using = LobbyPlayer.LobbyPlayerDeserializer.class)
    private HashMap<String, LobbyPlayer> players;

    public GameRoom() {
        players = new HashMap<>();
    }
    public void addPlayer(LobbyPlayer player) {
        players.put(player.getName(), player);
    }

    public void removePlayer(String username) {
        players.remove(username);
    }

    public List<LobbyPlayer> getPlayersAsList() {
        return new ArrayList<>(players.values());
    }
}
