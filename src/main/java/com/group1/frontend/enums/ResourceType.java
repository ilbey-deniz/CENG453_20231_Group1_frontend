package com.group1.frontend.enums;

import javafx.scene.paint.Paint;

public enum ResourceType {
    FOREST,
    HILLS,
    PASTURE,
    FIELDS,
    MOUNTAIN;

    public Paint getColor() {
        return switch (this) {
            case FOREST -> Paint.valueOf("#006400");
            case HILLS -> Paint.valueOf("#A52A2A");
            case PASTURE -> Paint.valueOf("#00FF00");
            case FIELDS -> Paint.valueOf("#FFD700");
            case MOUNTAIN -> Paint.valueOf("#808080");
            default -> Paint.valueOf("#000000");
        };
    }
    public String getImagePath() {
        return switch (this) {
            case FOREST -> "C:\\Users\\Mefebe\\IdeaProjects\\Frontend\\src\\main\\resources\\assets\\forest.png";
            case HILLS -> "C:\\Users\\Mefebe\\IdeaProjects\\Frontend\\src\\main\\resources\\assets\\hill.png";
            case PASTURE -> "C:\\Users\\Mefebe\\IdeaProjects\\Frontend\\src\\main\\resources\\assets\\pasteur.png";
            case FIELDS -> "C:\\Users\\Mefebe\\IdeaProjects\\Frontend\\src\\main\\resources\\assets\\field.png";
            case MOUNTAIN -> "C:\\Users\\Mefebe\\IdeaProjects\\Frontend\\src\\main\\resources\\assets\\mountain.png";
            default -> "src/main/resources/assets/forest.png";
        };
    }
}
