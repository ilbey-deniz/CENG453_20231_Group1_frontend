package com.group1.frontend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.group1.frontend.MainApplication;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.enums.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class LobbyPlayer {
    private String name;
    private ImageView colorImage;
    private PlayerColor color;
    private Boolean cpu;
    private Boolean ready;

    public LobbyPlayer(PlayerColor color, String username, Boolean cpu, Boolean ready) {
        this.name = username;
        this.cpu = cpu;
        this.color = color;
        setColorImage(color);
        this.ready = ready;
    }
    public LobbyPlayer(PlayerDto playerDto) {
        this.name = playerDto.getName();
        this.cpu = playerDto.isCpu();
        this.color = playerDto.getColor();
        setColorImage(color);
        this.ready = playerDto.isReady();
    }
    public void setColorImage(PlayerColor color) {
        String colorStr = color.toString();
        try {
            this.colorImage = new ImageView(new Image(MainApplication.class.getResource("/assets/" + colorStr + ".png").toString()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static class LobbyPlayerDeserializer extends JsonDeserializer<HashMap<String, LobbyPlayer>> {
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
}
