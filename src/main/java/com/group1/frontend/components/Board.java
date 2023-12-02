package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.group1.frontend.constants.BoardConstants.*;

public class Board {
    private final List<Tile> tiles = new ArrayList<Tile>();
    private List<Intersection> points = new ArrayList<Intersection>();
    private List<Edge> roads = new ArrayList<Edge>();

    public Board() {

    }

//    The game board will consist of 19 hexagons, distributed as follows: 1
//    desert, 3 hills, 3 mountains, 4 forests, 4 fields, and 4 pasture tiles.
//    • Each resource tile, excluding the desert, will be randomly placed on the
//    board, with the desert positioned at the center of the map.
//    • A number will be assigned to each resource tile, except the desert. This
//    includes one each of 2, 3, 11, and 12, and two each of 4, 5, 6, 7, 8, 9, and 10.
//    • Each tile provides resources, excluding the desert.
//    • Roads will connect hexagon tiles, forming a network of paths between them.
//    • Roads will also extend along the outer edges of the hexagons, creating a border around the entire board.
    public void generateRandomBoard() {

        for(double[] tileCoordinate : TILE_COORDINATES) {
            double x = tileCoordinate[0] * TILE_HEIGHT*V2/2 * 1.10 + xStartOffset;
            double y = tileCoordinate[1] * TILE_HEIGHT*3/4 * 1.10 + yStartOffset;
            ResourceType randomResourceType = ResourceType.values()[new Random().nextInt(ResourceType.values().length)];
            int randomDiceNumber = new Random().nextInt(11) + 2;
//            if (TILE_RESOURCE_TYPES.get(randomResourceType) == 0){
//                randomResourceType = ResourceType.values()[new Random().nextInt(ResourceType.values().length)];
//                while (TILE_RESOURCE_TYPES.get(randomResourceType) != 0){
//                    randomResourceType = ResourceType.values()[new Random().nextInt(ResourceType.values().length)];
//                }
//            }
            // pick random resource type and dice number and decrement its count
            Tile tile = new Tile(randomDiceNumber, randomResourceType, x, y);
            tiles.add(tile);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

}
