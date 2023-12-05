package com.group1.frontend.enums;

public enum TileType {
    FOREST,
    HILLS,
    PASTURE,
    FIELDS,
    DESERT,
    MOUNTAIN;

    public String getImagePath() {
        return switch (this) {
            case FOREST -> "src/main/resources/assets/forest.png";
            case HILLS -> "src/main/resources/assets/hill.png";
            case PASTURE -> "src/main/resources/assets/pasteur.png";
            case FIELDS -> "src/main/resources/assets/field.png";
            case DESERT -> "src/main/resources/assets/desert.png";
            case MOUNTAIN -> "src/main/resources/assets/mountain.png";
        };
    }
}
