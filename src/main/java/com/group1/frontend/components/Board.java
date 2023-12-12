package com.group1.frontend.components;

import com.group1.frontend.enums.TileType;

import java.util.*;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomKey;

public class Board {
    private final HashMap<List<Double> ,Tile> tiles;
    private final HashMap<List<Double> ,Corner> corners;
    private final List<Edge> edges;

    private final Set<List<Double>> points;
    private final List<List<List<Double>>> groupedCorners;


    public Board() {
        tiles = new HashMap<>();
        corners = new HashMap<>();
        edges = new ArrayList<>();
        points = new HashSet<>();
        groupedCorners = new ArrayList<>();
    }

    public void generateRandomBoard() {
        HashMap<TileType, Integer> tileResourceTypes = new HashMap<>(TILE_RESOURCE_TYPES);
        HashMap<Integer, Integer> tileDiceNumbers = new HashMap<> (TILE_DICE_NUMBERS);

        for(double[] tileCoordinate : TILE_COORDINATES) {
            double x = tileCoordinate[0];   double y = tileCoordinate[1];

            if( tileCoordinate[0] == 0 && tileCoordinate[1] == 0){
                // TODO: Not assign number to desert.
                Tile tile = new Tile(7, TileType.DESERT, x, y);
                tiles.put(Arrays.asList(x, y), tile);
                continue;
            }
            TileType randomResourceType = getRandomKey(tileResourceTypes);
            tileResourceTypes.put(randomResourceType, tileResourceTypes.get(randomResourceType) - 1);
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
            tiles.put(Arrays.asList(x, y),tile);
        }
        getCornersFromTileCoordinates();
        for (List<Double> cornerCoordinates : points) {
            Corner corner = new Corner(cornerCoordinates.get(0), cornerCoordinates.get(1));
            corners.put(cornerCoordinates, corner);
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
            edges.add(edge);
        }
    }

    public void getCornersFromTileCoordinates(){
        for(double[] tileCoordinate : TILE_COORDINATES) {
            double x = tileCoordinate[0] * 2;
            double y = tileCoordinate[1] * 3;
            points.add(Arrays.asList(x, y-2));
            points.add(Arrays.asList(x-1, y-1));
            points.add(Arrays.asList(x-1, y+1));
            points.add(Arrays.asList(x, y+2));
            points.add(Arrays.asList(x+1, y+1));
            points.add(Arrays.asList(x+1, y-1));

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
        return new ArrayList<>(tiles.values());
    }


    public List<Corner> getCorners() {
        return new ArrayList<>(corners.values());
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public List<Corner> getAdjacentCornersOfEdge(Edge e) {
        //for first coordinate pair
        List<Corner> adjacentCorners = new ArrayList<>();
        adjacentCorners.add(corners.get(Arrays.asList(e.getFirstXCoordinate(), e.getFirstYCoordinate())));
        adjacentCorners.add(corners.get(Arrays.asList(e.getSecondXCoordinate(), e.getSecondYCoordinate())));
        return adjacentCorners;

    }

    public List<Corner> getAdjacentCornersOfCorner(Corner c){
        List<Corner> adjacentCorners = new ArrayList<>();

        int mod = ((int) c.getYCoordinate())%3;
        if(mod < 0){
            mod += 3;
        }

        if(mod == 1){
            Corner cornerToAdd = corners.get(Arrays.asList(c.getXCoordinate(), c.getYCoordinate()-2));
            if(cornerToAdd != null){
                adjacentCorners.add(cornerToAdd);
            }
            Corner cornerToAdd2 = corners.get(Arrays.asList(c.getXCoordinate()-1, c.getYCoordinate()+1));
            if(cornerToAdd2 != null){
                adjacentCorners.add(cornerToAdd2);
            }
            Corner cornerToAdd3 = corners.get(Arrays.asList(c.getXCoordinate()+1, c.getYCoordinate()+1));
            if(cornerToAdd3 != null){
                adjacentCorners.add(cornerToAdd3);
            }
            return adjacentCorners;
        }

        if (mod == 2){
            Corner cornerToAdd = corners.get(Arrays.asList(c.getXCoordinate(), c.getYCoordinate()+2));
            if(cornerToAdd != null){
                adjacentCorners.add(cornerToAdd);
            }
            Corner cornerToAdd2 = corners.get(Arrays.asList(c.getXCoordinate()-1, c.getYCoordinate()-1));
            if(cornerToAdd2 != null){
                adjacentCorners.add(cornerToAdd2);
            }
            Corner cornerToAdd3 = corners.get(Arrays.asList(c.getXCoordinate()+1, c.getYCoordinate()-1));
            if(cornerToAdd3 != null){
                adjacentCorners.add(cornerToAdd3);
            }
            return adjacentCorners;
        }
        return adjacentCorners;
    }

    public List<Edge> getAdjacentEdgesOfCorner(Corner c) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for(Edge edge : getEdges()) {
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
        for(Edge edge : getEdges()) {
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
        List<Tile> adjacentTiles = new ArrayList<>();
        int mod = ((int) c.getYCoordinate())%3;
        if(mod < 0){
            mod += 3;
        }
        if (mod == 1) {
            double topYCoordinate = (c.getYCoordinate() + 2.0) / 3;
            double topXCoordinate = c.getXCoordinate() / 2.0;
            Tile tileToAdd = tiles.get(Arrays.asList(topXCoordinate, topYCoordinate));
            if(tileToAdd != null){
                adjacentTiles.add(tileToAdd);
//                System.out.println(tileToAdd.getResourceType());
            }
            Tile tileToAdd2 = tiles.get(Arrays.asList(topXCoordinate-0.5, topYCoordinate-1));
            if (tileToAdd2 != null) {
                adjacentTiles.add(tileToAdd2);
//                System.out.println(tileToAdd2.getResourceType());
            }
            Tile tileToAdd3 = tiles.get(Arrays.asList(topXCoordinate+0.5, topYCoordinate-1));
            if (tileToAdd3 != null) {
                adjacentTiles.add(tileToAdd3);
//                System.out.println(tileToAdd3.getResourceType());
            }
            return adjacentTiles;
        } else if (mod == 2) {
            double bottomYCoordinate = (c.getYCoordinate() - 2.0) / 3;
            double bottomXCoordinate = c.getXCoordinate() / 2.0;
            Tile tileToAdd = tiles.get(Arrays.asList(bottomXCoordinate, bottomYCoordinate));
            if(tileToAdd != null){
                adjacentTiles.add(tileToAdd);
//                System.out.println(tileToAdd.getResourceType());
            }
            Tile tileToAdd2 = tiles.get(Arrays.asList(bottomXCoordinate-0.5, bottomYCoordinate+1));
            if (tileToAdd2 != null) {
                adjacentTiles.add(tileToAdd2);
//                System.out.println(tileToAdd2.getResourceType());
            }
            Tile tileToAdd3 = tiles.get(Arrays.asList(bottomXCoordinate+0.5, bottomYCoordinate+1));
            if (tileToAdd3 != null) {
                adjacentTiles.add(tileToAdd3);
//                System.out.println(tileToAdd3.getResourceType());
            }
            return adjacentTiles;
        }
        return adjacentTiles;
    }

}
