package com.group1.frontend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.group1.frontend.enums.PlayerColor;

import java.util.HashMap;

public class JsonDeserializers {
    public static class PlayersHashMapDeserializer extends JsonDeserializer<HashMap<String, LobbyPlayer>> {
        @Override
        public HashMap<String, LobbyPlayer> deserialize(JsonParser jsonParser,
                                                        DeserializationContext deserializationContext) throws java.io.IOException{

            HashMap<String, LobbyPlayer> players = new HashMap<>();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            for (JsonNode playerNode : node) {
                String name = playerNode.get("name").asText();
                PlayerColor color = PlayerColor.valueOf(playerNode.get("color").asText());
                Boolean cpu = playerNode.get("cpu").asBoolean();
                Boolean ready = playerNode.get("ready").asBoolean();
                players.put(name, new LobbyPlayer(color, name, cpu, ready));
            }
            return players;
        }
    }
    public static class LobbyPlayerDeserializer extends JsonDeserializer<LobbyPlayer> {
        @Override
        public LobbyPlayer deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext) throws java.io.IOException{

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String name = node.get("name").asText();
            PlayerColor color = PlayerColor.valueOf(node.get("color").asText());
            Boolean cpu = node.get("cpu").asBoolean();
            Boolean ready = node.get("ready").asBoolean();
            return new LobbyPlayer(color, name, cpu, ready);
        }
    }
}
