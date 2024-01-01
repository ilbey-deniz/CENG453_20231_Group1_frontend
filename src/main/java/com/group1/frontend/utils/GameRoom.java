package com.group1.frontend.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @JsonDeserialize(using = JsonDeserializers.PlayersHashMapDeserializer.class)
    private HashMap<String, LobbyPlayer> players;

    public GameRoom() {
        players = new HashMap<>();
    }
    public void addPlayer(LobbyPlayer player) {
        players.put(player.getName(), player);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    public List<LobbyPlayer> getPlayersAsList() {
        return new ArrayList<>(players.values());
    }
}
