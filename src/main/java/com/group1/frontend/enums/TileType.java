package com.group1.frontend.enums;

import com.group1.frontend.MainApplication;

import java.net.URL;

public enum TileType {
    FOREST,
    HILLS,
    PASTURE,
    FIELDS,
    DESERT,
    MOUNTAIN;

    public URL getImagePath() {
        return switch (this) {
            case FOREST -> MainApplication.class.getResource("/assets/forest.png");
            case HILLS -> MainApplication.class.getResource("/assets/hills.png");
            case PASTURE -> MainApplication.class.getResource("/assets/pasture.png");
            case FIELDS -> MainApplication.class.getResource("/assets/fields.png");
            case DESERT -> MainApplication.class.getResource("/assets/desert.png");
            case MOUNTAIN -> MainApplication.class.getResource("/assets/mountain.png");
        };
    }

    public ResourceType getResourceType() {
        return switch (this) {
            case FOREST -> ResourceType.LUMBER;
            case HILLS -> ResourceType.BRICK;
            case PASTURE -> ResourceType.WOOL;
            case FIELDS -> ResourceType.GRAIN;
            case DESERT -> null;
            case MOUNTAIN -> ResourceType.ORE;
        };
    }
}
