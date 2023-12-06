package com.group1.frontend.components;

import com.group1.frontend.enums.TileType;

import java.util.*;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomKey;

public class Board {
    private final List<Tile> tiles = new ArrayList<>();
    private List<Corner> points = new ArrayList<>();

    //TODO: Edges are duplicated. Need to fix this.
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
        HashMap<TileType, Integer> tileResourceTypes = new HashMap<>(TILE_RESOURCE_TYPES);
        HashMap<Integer, Integer> tileDiceNumbers = new HashMap<> (TILE_DICE_NUMBERS);

        for(double[] tileCoordinate : TILE_COORDINATES) { double x = tileCoordinate[0];   double y = tileCoordinate[1];   if( tileCoordinate[0] == 0 && tileCoordinate[1] == 0){
                // TODO: Not assign number to desert.
                Tile tile = new Tile(7, TileType.DESERT, x, y);
                tiles.add(tile);
                continue;
            }
            TileType randomResourceType = getRandomKey(tileResourceTypes);tileResourceTypes.put(randomResourceType, tileResourceTypes.get(randomResourceType) - 1);
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
        // create a sorted set to remove duplicate edges from the list
        Set<List<Double>> nonDuplicateEdges = new HashSet<>();
        for(List<List<Double>> groupedCorner : groupedCorners){
            for(int i = 0; i < groupedCorner.size(); i++){
                List<Double> edgeCoordinates = new ArrayList<>();
                if(groupedCorner.get(i).get(1) > groupedCorner.get((i+1)%groupedCorner.size()).get(1)){
                    edgeCoordinates.add(groupedCorner.get((i+1)%groupedCorner.size()).get(0));
                    edgeCoordinates.add(groupedCorner.get((i+1)%groupedCorner.size()).get(1));
                    edgeCoordinates.add(groupedCorner.get(i).get(0));
                    edgeCoordinates.add(groupedCorner.get(i).get(1));
                    nonDuplicateEdges.add(edgeCoordinates);
                    continue;
                }
                edgeCoordinates.add(groupedCorner.get(i).get(0));
                edgeCoordinates.add(groupedCorner.get(i).get(1));
                edgeCoordinates.add(groupedCorner.get((i+1)%groupedCorner.size()).get(0));
                edgeCoordinates.add(groupedCorner.get((i+1)%groupedCorner.size()).get(1));

                nonDuplicateEdges.add(edgeCoordinates);
            }
        }
        for (List<Double> edgesCoordinates : nonDuplicateEdges){
            Edge edge = new Edge(edgesCoordinates.get(0), edgesCoordinates.get(1), edgesCoordinates.get(2), edgesCoordinates.get(3));
            roads.add(edge);
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
//    public void printAllCornerOfIsOccupied() {
//        for (Corner corner : points) {
//            System.out.println(corner.getIsOccupied());
//        }
//
//    }


    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Corner> getPoints() {
        return points;
    }

    public List<Edge> getEdges() {
        return roads;
    }

    public List<Corner> getAdjacentCornersOfEdge(Edge e) {
        //for first coordinate pair
        List<Corner> adjacentCorners = new ArrayList<>();
        for(Corner corner : points) {
            if (corner.getXCoordinate() == e.getFirstXCoordinate() && corner.getYCoordinate() == e.getFirstYCoordinate()) {
                adjacentCorners.add(corner);
            } else if (corner.getXCoordinate() == e.getSecondXCoordinate() && corner.getYCoordinate() == e.getSecondYCoordinate()) {
                adjacentCorners.add(corner);
            }
        }
        return adjacentCorners;

    }

    public List<Edge> getAdjacentEdgesOfCorner(Corner c) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for(Edge edge : roads) {
            if (edge.getFirstXCoordinate() == c.getXCoordinate() && edge.getFirstYCoordinate() == c.getYCoordinate()) {
                adjacentEdges.add(edge);
            } else if (edge.getSecondXCoordinate() == c.getXCoordinate() && edge.getSecondYCoordinate() == c.getYCoordinate()) {
                adjacentEdges.add(edge);
            }
        }
            return adjacentEdges;
    }

    public List<Edge> getAdjacentEdgesOfEdge(Edge e){
        List<Edge> adjacentEdges = new ArrayList<>();
        for(Edge edge : roads) {
            if (edge.getFirstXCoordinate() == e.getFirstXCoordinate() && edge.getFirstYCoordinate() == e.getFirstYCoordinate()) {
                adjacentEdges.add(edge);
            } else if (edge.getSecondXCoordinate() == e.getSecondXCoordinate() && edge.getSecondYCoordinate() == e.getSecondYCoordinate()) {
                adjacentEdges.add(edge);
            } else if (edge.getFirstXCoordinate() == e.getSecondXCoordinate() && edge.getFirstYCoordinate() == e.getSecondYCoordinate()) {
                adjacentEdges.add(edge);
            } else if (edge.getSecondXCoordinate() == e.getFirstXCoordinate() && edge.getSecondYCoordinate() == e.getFirstYCoordinate()) {
                adjacentEdges.add(edge);
            }
        }
        return adjacentEdges;
    }

    public List<Tile> getAdjacentTilesOfCorner(Corner c) {
        // TODO: implement this by detecting center of tile
        //  (2 possible arrangement upward, downward), then find the tile by using its center value
        List<Tile> adjacentTiles = new ArrayList<>();
        int mod = ((int) c.getYCoordinate())%3;
        if(mod < 0){
            mod += 3;
        }
        for(Tile tile : tiles) {
            if (mod == 1){  // need to look at upward, left down, right down
                double topYCoordinate = (c.getYCoordinate() + 2.0) / 3;
                double topXCoordinate = c.getXCoordinate() / 2.0;
                if(tile.getXCoordinate() == topXCoordinate && tile.getYCoordinate() == topYCoordinate){
                    adjacentTiles.add(tile);
                } else if (tile.getXCoordinate() == topXCoordinate-0.5 && tile.getYCoordinate() == topYCoordinate - 1) {
                    adjacentTiles.add(tile);
                } else if (tile.getXCoordinate() == topXCoordinate+0.5 && tile.getYCoordinate() == topYCoordinate - 1) {
                    adjacentTiles.add(tile);
                }
            } else if (mod == 2) { //  need to look at downward, left up, right up
                double bottomYCoordinate = (c.getYCoordinate() - 2.0) / 3;
                double bottomXCoordinate = c.getXCoordinate() / 2.0;
                if(tile.getXCoordinate() == bottomXCoordinate && tile.getYCoordinate() == bottomYCoordinate){
                    adjacentTiles.add(tile);
                } else if (tile.getXCoordinate() == bottomXCoordinate-0.5 && tile.getYCoordinate() == bottomYCoordinate + 1) {
                    adjacentTiles.add(tile);
                } else if (tile.getXCoordinate() == bottomXCoordinate+0.5 && tile.getYCoordinate() == bottomYCoordinate + 1) {
                    adjacentTiles.add(tile);
                }
            }
        }
        return adjacentTiles;
    }
}
