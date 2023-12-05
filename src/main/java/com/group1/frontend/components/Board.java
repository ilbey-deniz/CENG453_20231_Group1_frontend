package com.group1.frontend.components;

import com.group1.frontend.enums.ResourceType;

import java.io.DataOutput;
import java.util.*;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomKey;

public class Board {
    private final List<Tile> tiles = new ArrayList<>();
    private List<Corner> points = new ArrayList<>();
    private List<Edge> roads = new ArrayList<>();

    Set<List<Double>> corners = new HashSet<>();
    List<List<List<Double>>> groupedCorners = new ArrayList<>();

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
        HashMap<ResourceType, Integer> tileResourceTypes = new HashMap<>(TILE_RESOURCE_TYPES);
        HashMap<Integer, Integer> tileDiceNumbers = new HashMap<> (TILE_DICE_NUMBERS);

        for(double[] tileCoordinate : TILE_COORDINATES) { double x = tileCoordinate[0];   double y = tileCoordinate[1];   if( tileCoordinate[0] == 0 && tileCoordinate[1] == 0){
                // TODO: Not assign number to desert.
                Tile tile = new Tile(7, ResourceType.DESERT, x, y);
                tiles.add(tile);
                continue;
            }
            ResourceType randomResourceType = getRandomKey(tileResourceTypes);tileResourceTypes.put(randomResourceType, tileResourceTypes.get(randomResourceType) - 1);
            Integer randomDiceNumber = getRandomKey(tileDiceNumbers);
            tileDiceNumbers.put(randomDiceNumber, tileDiceNumbers.get(randomDiceNumber) - 1);
            if(tileResourceTypes.get(randomResourceType) == 0){
                tileResourceTypes.remove(randomResourceType);
            }
            if(tileDiceNumbers.get(randomDiceNumber) == 0){
                tileDiceNumbers.remove(randomDiceNumber);
            }
            assert randomDiceNumber != null;
            Tile tile = new Tile(randomDiceNumber, randomResourceType, x, y);
            tiles.add(tile);
        }
        getCornersFromTileCoordinates();
        for (List<Double> cornerCoordinates : corners) {
            Corner corner = new Corner(cornerCoordinates.get(0), cornerCoordinates.get(1));
            points.add(corner);
        }

        for(List<List<Double>> groupedCorner : groupedCorners){
            for(int i = 0; i < groupedCorner.size(); i++){
                Edge edge = new Edge(groupedCorner.get(i).get(0), groupedCorner.get(i).get(1),
                        groupedCorner.get((i+1)%groupedCorner.size()).get(0),
                        groupedCorner.get((i+1)%groupedCorner.size()).get(1));
                roads.add(edge);
            }
        }
    }

    public void getCornersFromTileCoordinates(){
        for(double[] tileCoordinate : TILE_COORDINATES) {
            double x = tileCoordinate[0] * 2;
            double y = tileCoordinate[1] * 3;
            corners.add(Arrays.asList(x, y-2));
            corners.add(Arrays.asList(x-1, y-1));
            corners.add(Arrays.asList(x-1, y+1));
            corners.add(Arrays.asList(x, y+2));
            corners.add(Arrays.asList(x+1, y+1));
            corners.add(Arrays.asList(x+1, y-1));

            List<List<Double>> groupedCorner = new ArrayList<>();
            groupedCorner.add(Arrays.asList(x, y-2));
            groupedCorner.add(Arrays.asList(x-1, y-1));
            groupedCorner.add(Arrays.asList(x-1, y+1));
            groupedCorner.add(Arrays.asList(x, y+2));
            groupedCorner.add(Arrays.asList(x+1, y+1));
            groupedCorner.add(Arrays.asList(x+1, y-1));
            groupedCorners.add(groupedCorner);
        }
    }


    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Corner> getPoints() {
        return points;
    }

    public List<Edge> getEdges() {
        return roads;
    }
}
