package com.group1.frontend.enums;

import javafx.scene.paint.Paint;

public enum ResourceType {
    WOOD,
    BRICK,
    SHEEP,
    WHEAT,
    ORE;

    public Paint getColor() {
        switch (this) {
            case WOOD:
                return Paint.valueOf("#006400");
            case BRICK:
                return Paint.valueOf("#A52A2A");
            case SHEEP:
                return Paint.valueOf("#00FF00");
            case WHEAT:
                return Paint.valueOf("#FFD700");
            case ORE:
                return Paint.valueOf("#808080");
            default:
                return Paint.valueOf("#000000");
        }
    }
}
