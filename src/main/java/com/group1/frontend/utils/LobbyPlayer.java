package com.group1.frontend.utils;

import com.group1.frontend.MainApplication;
import com.group1.frontend.constants.ApplicationConstants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LobbyPlayer {
    private String username;
    private ImageView color;
    private String ready;

    public LobbyPlayer(String color, String username,  String ready) {
        this.username = username;
        setColor(color);
        this.ready = ready;
    }
    //getters and setters
    public String getUsername() {
        return username;
    }
    public ImageView getColor() {
        return color;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setColor(String color){
        color = color.toLowerCase();
        try {
            this.color = new ImageView(new Image(MainApplication.class.getResource("/assets/" + color + ".png").toString()));
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }
    public String getReady() {
        return ready;
    }
    public void setReady(String ready) {
        this.ready = ready;
    }
}
