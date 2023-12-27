package com.group1.frontend.utils;

import com.group1.frontend.MainApplication;
import com.group1.frontend.constants.ApplicationConstants;
import com.group1.frontend.enums.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Getter
@Setter
public class LobbyPlayer {
    private String username;
    private ImageView color;
    private Boolean cpu;
    private String ready;

    public LobbyPlayer(PlayerColor color, String username, Boolean cpu, String ready) {
        this.username = username;
        this.cpu = cpu;
        setColor(color);
        this.ready = ready;
    }
    public void setColor(PlayerColor color) {
        String colorStr = color.toString().toLowerCase();
        try {
            this.color = new ImageView(new Image(MainApplication.class.getResource("/assets/" + colorStr + ".png").toString()));
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }
}
