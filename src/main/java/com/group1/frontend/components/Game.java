package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.exceptions.DiceAlreadyRolledException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.group1.frontend.constants.BoardConstants.REQUIRED_RESOURCES;

public class Game {
    private List<Player> players;
    private Board board;

    private final HashSet<Building> occupiedBuildings;
    private Player currentPlayer;
    private int turnNumber;

    private Pair<Integer, Integer> currentDiceRoll;

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayer = players.get(0);
        this.turnNumber = 0;
        this.occupiedBuildings = new HashSet<>();
    }
    public Game() {
        this.players = new ArrayList<>();
        this.board = new Board();
        this.currentPlayer = null;
        this.turnNumber = 0;
        this.occupiedBuildings = new HashSet<>();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void addPlayer(String color, String name, Boolean cpu) {
        players.add(new Player(name, color, cpu));
    }

    public void removePlayer(Player player) {
        //TODO: if that is a real player, replace it with a CPU player
        players.remove(player);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void incrementTurnNumber() {
        turnNumber++;
    }

    public int getTurnNumber() {
        return turnNumber;
    }


    public boolean isEdgeAvailableToBuild(Edge e){
        // check whether edge is adjacent to another edge or corner building owned by player
        // first look at adjacent corners. whether they are occupied by opponent player we can not build.
        // if not occupied by opponent player then we can build road
        // second look at edges adjacent to the corners. If they are occupied by our player then we can build road
        return false;
    }



    // Wrong function. It does not check if the corner is adjacent to any of the player's buildings.
    private boolean isCornerAvailableToBuild(Corner c) {
        // check 3 of adjacent edge is occupied by player. If yes, return true.
        List<Edge> adjacentEdges = board.getAdjacentEdgesOfCorner(c);
        for(Edge edge : adjacentEdges) {
            if (edge.getOwner() == currentPlayer) {
                return true;
            }
        }
        return false;
    }

    //1. get player's road list
    //2. for each road, getAdjacentEdgesOfEdge and check if they are not occupied. if not occupied, add to availableEdges HashSet
    //TODO: getAdjacentEdgesOfEdge function finds same edge multiple times, bad performance
    //TODO: storing availableEdges for each player as a HashSet is better
    public List<Edge> getAvailableEdges() {
        HashSet<Edge> availableEdges = new HashSet<>();
        for(Road road : currentPlayer.roads) {
            List<Edge> adjacentEdges = board.getAdjacentEdgesOfEdge(road.getEdge());
            for(Edge edge : adjacentEdges) {
                if (!edge.isOccupied()) {
                    availableEdges.add(edge);
                }
            }
        }
        return new ArrayList<>(availableEdges);
    }

    //1.get player's road list
    //2.for each road, getAdjacentCornersOfEdge and check if they are not occupied. if not occupied, add to availableCorners HashSet
    //3.get every building's list (all players')
    //4.for each building's corners, getAdjacentCornersOfCorner and remove them from availableCorners HashSet

    public List<Corner> getAvailableCorners() {
        HashSet<Corner> availableCorners = new HashSet<>();
        for(Road road : currentPlayer.roads) {
            List<Corner> adjacentCorners = board.getAdjacentCornersOfEdge(road.getEdge());
            for(Corner corner : adjacentCorners) {
                if (!corner.getIsOccupied()) {
                    availableCorners.add(corner);
                }
            }
        }
        for(Player player : players) {
            for(Building building : player.buildings) {
                List<Corner> adjacentCorners = board.getAdjacentCornersOfCorner(building.getCorner());
                for(Corner corner : adjacentCorners) {
                    availableCorners.remove(corner);
                }
            }
        }
        return new ArrayList<>(availableCorners);
    }

    // set each player current player at for loop
    // for each player, get random building from board.points
    // for each point, check if it is available to build
    // if available, build settlement
    // ! Be careful about checking nearby buildings.
    // If there is a building nearby, it should not be available to build
    // find the adjacent edges of the settlement
    // for each edge, check if it is available to build
    // if available, build road one of the adjacent edges randomly.
    public void createInitialBuildings() {
        for(Player player : players) {
            player.setCpu(true);
            Corner randomCorner = board.getCorners().get((int) (Math.random() * board.getCorners().size()));
            while(!isCornerAvailableToInitialize(randomCorner)) {
                randomCorner = board.getCorners().get((int) (Math.random() * board.getCorners().size()));
            }
            placeSettlementForFree(randomCorner, player);
            List<Edge> adjacentEdges = board.getAdjacentEdgesOfCorner(randomCorner);
            Edge randomEdge = adjacentEdges.get((int) (Math.random() * adjacentEdges.size()));
            placeRoadForFree(randomEdge, player);
        }
    }

    private boolean isCornerAvailableToInitialize(Corner randomCorner) {
        AtomicBoolean isAvailable = new AtomicBoolean(true);
        occupiedBuildings.forEach(building -> {
            if (building.getCorner().equals(randomCorner)) {
                isAvailable.set(false);
            }else {
                List<Corner> adjacentCorners = board.getAdjacentCornersOfCorner(building.getCorner());
                adjacentCorners.forEach(corner -> {
                    if (corner.equals(randomCorner)) {
                        isAvailable.set(false);
                    }
                });
            }
        });
        return isAvailable.get();
    }

    public Pair<Integer, Integer> rollDice() throws DiceAlreadyRolledException {
        if (currentDiceRoll != null) {
            throw new DiceAlreadyRolledException("Dice already rolled this turn.");
        }
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        currentDiceRoll = new Pair<>(dice1, dice2);
        return new Pair<>(dice1, dice2);
    }

    public void endTurn() {
        currentDiceRoll = null;
        //currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        turnNumber++;
    }

    public void placeSettlement(Corner corner, Player player) {
        corner.setIsOccupied(true);
        corner.setOwner(player);
        REQUIRED_RESOURCES.get(BuildingType.SETTLEMENT).forEach(player::removeResource);
        Building building = new Building(BuildingType.SETTLEMENT, player, board.getAdjacentTilesOfCorner(corner), corner);
        player.buildings.add(building);
        occupiedBuildings.add(building);
    }
    public void placeSettlementForFree(Corner corner, Player player) {
        corner.setIsOccupied(true);
        corner.setOwner(player);
        List<Tile> adjacentTiles = board.getAdjacentTilesOfCorner(corner);
        Building building = new Building(BuildingType.SETTLEMENT, player, adjacentTiles, corner);
        player.buildings.add(building);
        occupiedBuildings.add(building);
        for(Tile tile : adjacentTiles) {
            if (tile.getDiceNumber() != 7){
                player.addResource(tile.getTileType().getResourceType(), 1);
            }
        }
    }

    public void placeRoad(Edge edge, Player player) {
        edge.setOccupied(true);
        edge.setOwner(player);
        REQUIRED_RESOURCES.get(BuildingType.ROAD).forEach(player::removeResource);
        player.roads.add(new Road(player, edge));
    }

    public void placeRoadForFree(Edge randomEdge, Player player) {
        randomEdge.setOccupied(true);
        randomEdge.setOwner(player);
        player.roads.add(new Road(player, randomEdge));
    }
}
