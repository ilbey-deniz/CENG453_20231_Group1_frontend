package com.group1.frontend.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.MainApplication;
import com.group1.frontend.dto.httpDto.PlayerDto;
import com.group1.frontend.enums.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = JsonDeserializers.LobbyPlayerDeserializer.class)
public class LobbyPlayer {
    private String name;
    @JsonIgnore
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

    public LobbyPlayer() {
        this.name = "";
        this.cpu = false;
        this.color = PlayerColor.red;
        this.ready = false;
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


}
