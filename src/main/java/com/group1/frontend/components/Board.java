package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

import java.util.ArrayList;
import java.util.List;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomKey;

public class Board {
    private final List<Tile> tiles = new ArrayList<>();
    private List<Intersection> points = new ArrayList<>();
    private List<Edge> roads = new ArrayList<>();

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
            double x = tileCoordinate[0];
            double y = tileCoordinate[1];
            if( tileCoordinate[0] == 0 && tileCoordinate[1] == 0){
                Tile tile = new Tile(7, ResourceType.DESERT, x, y);
                tiles.add(tile);
                continue;
            }

            ResourceType randomResourceType = getRandomKey(TILE_RESOURCE_TYPES);
            TILE_RESOURCE_TYPES.put(randomResourceType, TILE_RESOURCE_TYPES.get(randomResourceType) - 1);

//            int randomDiceNumber = new Random().nextInt(11) + 2;
            Integer randomDiceNumber = getRandomKey(TILE_DICE_NUMBERS);
            TILE_DICE_NUMBERS.put(randomDiceNumber, TILE_DICE_NUMBERS.get(randomDiceNumber) - 1);

            if(TILE_RESOURCE_TYPES.get(randomResourceType) == 0){
                TILE_RESOURCE_TYPES.remove(randomResourceType);
            }

            if(TILE_DICE_NUMBERS.get(randomDiceNumber) == 0){
                TILE_DICE_NUMBERS.remove(randomDiceNumber);
            }

            // pick random resource type and dice number and decrement its count
            System.out.println(randomResourceType);

            assert randomDiceNumber != null;
            Tile tile = new Tile(randomDiceNumber, randomResourceType, x, y);
            tiles.add(tile);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

}
