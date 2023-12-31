package com.group1.frontend.enums;

//why its lowercase? - because it matches the file names of the images
public enum PlayerColor {
    red,
    blue,
    yellow,
    green;

    public static PlayerColor getRandomColor() {
        return values()[(int) (Math.random() * values().length)];
    }
}
