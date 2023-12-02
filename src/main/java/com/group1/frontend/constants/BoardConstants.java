package com.group1.frontend.constants;

import com.group1.frontend.enums.ResourceType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Map;

public final class BoardConstants {

    public static final double WINDOW_WIDTH = Screen.getScreens().get(0).getBounds().getWidth();
    public static final double WINDOW_HEIGHT = Screen.getScreens().get(0).getBounds().getHeight()-77;
    public static final double xStartOffset = WINDOW_WIDTH/2; // offsets the entire field to the right
    public static final double yStartOffset = WINDOW_HEIGHT/2; // offsets the entire fiels downwards
    public static double TILE_HEIGHT = 150;
    public static final double V = Math.sqrt(3) / 2.0;
    public static final double V2 = Math.sqrt(3);
    public static final int TILE_COUNT = 19;

    public static final Map<ResourceType, Integer> TILE_RESOURCE_TYPES = Map.of(
        ResourceType.HILLS, 3,
        ResourceType.MOUNTAIN, 3,
        ResourceType.FOREST, 4,
        ResourceType.FIELDS, 4,
        ResourceType.PASTURE, 4
    );

    public static final Map<String, Integer> TILE_DICE_NUMBERS = Map.of(
        "2", 1,
        "3", 2,
        "4", 2,
        "5", 2,
        "6", 2,
        "8", 2,
        "9", 2,
        "10", 2,
        "11", 2,
        "12", 1
    );

    public static final double[][] TILE_COORDINATES = {
        {-1, -2}, {0, -2}, {1, -2},
        {-1.5, -1}, {-0.5, -1}, {0.5, -1}, {1.5, -1},
        {-2, 0}, {-1, 0}, {0, 0}, {1, 0}, {2, 0},
        {-1.5, 1}, {-0.5, 1}, {0.5, 1}, {1.5, 1},
        {-1, 2}, {0, 2}, {1, 2}
    };

}
