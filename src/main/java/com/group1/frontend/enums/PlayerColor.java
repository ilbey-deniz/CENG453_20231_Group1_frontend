package com.group1.frontend.enums;

public enum PlayerColor {
    red,
    blue,
    yellow,
    green;

    public static PlayerColor getRandomColor() {
        return values()[(int) (Math.random() * values().length)];
    }
}
