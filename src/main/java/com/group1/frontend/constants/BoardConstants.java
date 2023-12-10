package com.group1.frontend.constants;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.enums.TileType;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.*;

public final class BoardConstants {

    public static final int TURN_TIME = 5*60;
    public static final int DEFAULT_WIDTH = 1280;
    public static final int DEFAULT_HEIGHT = 720;
    public static final double WINDOW_WIDTH = Screen.getScreens().get(0).getBounds().getWidth();
    public static final double WINDOW_HEIGHT = Screen.getScreens().get(0).getBounds().getHeight()-77;
    //public static final double WINDOW_WIDTH = DEFAULT_WIDTH;
    //public static final double WINDOW_HEIGHT = DEFAULT_HEIGHT;

    public static final double xStartOffset = WINDOW_WIDTH/2; // offsets the entire field to the right
    public static final double yStartOffset = WINDOW_HEIGHT/2; // offsets the entire fiels downwards
    public static double TILE_HEIGHT = WINDOW_HEIGHT / 6.0; // height of the tile
    public static final double V = Math.sqrt(3) / 2.0;
    public static final double V2 = Math.sqrt(3);

    public static final Map<String , String > PLAYER_COLORS = new HashMap<>(Map.of(
        "red", "#ff4f4f",
        "blue", "#4fa6eb",
        "green", "#00ff00",
        "yellow", "#ffff00"
    ));

    public static final String BACKGROUND_ISLAND_COLOR = "#fff7d0";
    public static final String HIGHLIGHT_FILL_COLOR = Color.DARKGREY.toString();
    public static final String HIGHLIGHT_STROKE_COLOR = Color.RED.toString();

    public static HashMap<TileType, Integer> TILE_RESOURCE_TYPES = new HashMap<>(Map.of(
        TileType.HILLS, 3,
        TileType.MOUNTAIN, 3,
        TileType.FOREST, 4,
        TileType.FIELDS, 4,
        TileType.PASTURE, 4
    ));

    public static HashMap<BuildingType, HashMap<ResourceType, Integer>> REQUIRED_RESOURCES = new HashMap<>(Map.of(
        BuildingType.SETTLEMENT, new HashMap<>(Map.of(
            ResourceType.BRICK, 1,
            ResourceType.LUMBER, 1,
            ResourceType.WOOL, 1,
            ResourceType.GRAIN, 1
        )),
        BuildingType.CITY, new HashMap<>(Map.of(
            ResourceType.ORE, 3,
            ResourceType.GRAIN, 2
        )),
        BuildingType.ROAD, new HashMap<>(Map.of(
            ResourceType.BRICK, 1,
            ResourceType.LUMBER, 1
        ))
    ));

    public static HashMap<Integer, Integer> TILE_DICE_NUMBERS = new HashMap<> (Map.of(
        2, 1,
        3, 2,
        4, 2,
        5, 2,
        6, 2,
        8, 2,
        9, 2,
        10, 2,
        11, 2,
        12, 1
    ));


    // Coordinates of the tiles on the board
    public static final double[][] TILE_COORDINATES = {
        {-1, -2}, {0, -2}, {1, -2},
        {-1.5, -1}, {-0.5, -1}, {0.5, -1}, {1.5, -1},
        {-2, 0}, {-1, 0}, {0, 0}, {1, 0}, {2, 0},
        {-1.5, 1}, {-0.5, 1}, {0.5, 1}, {1.5, 1},
        {-1, 2}, {0, 2}, {1, 2}
    };

}


