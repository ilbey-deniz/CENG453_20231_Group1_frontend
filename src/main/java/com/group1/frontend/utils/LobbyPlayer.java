package com.group1.frontend.utils;

import com.group1.frontend.MainApplication;
import com.group1.frontend.enums.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LobbyPlayer {
    private String username;
    private ImageView colorImage;
    private PlayerColor color;
    private Boolean host;
    private Boolean cpu;
    private Boolean ready;

    public LobbyPlayer(PlayerColor color, String username, Boolean host, Boolean cpu, Boolean ready) {
        this.username = username;
        this.cpu = cpu;
        this.color = color;
        this.host = host;
        setColorImage(color);
        this.ready = ready;
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
    public void setHost(boolean b) {

    }
}
