package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.exceptions.DiceAlreadyRolledException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Game {
    private List<Player> players;
    private Board board;
    private Player currentPlayer;
    private int turnNumber;

    private Pair<Integer, Integer> currentDiceRoll;

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayer = players.get(0);
        this.turnNumber = 0;
    }
    public Game() {

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


    public String handleBuildRoad(Edge e) {
        // TODO: Rerun CalculateLongestRoad() function.
        if (currentPlayer.resources.get(ResourceType.BRICK) < 1 || currentPlayer.resources.get(ResourceType.LUMBER) < 1) {
            return "Not enough resources to build road";
        } else if (e.isOccupied()) {
            return "Edge is already occupied";
        }
        else if (!isEdgeAvailableToBuild(e)) {
            return "Edge is not adjacent to any of your buildings";
        }
        else {
            currentPlayer.resources.put(ResourceType.BRICK, currentPlayer.resources.get(ResourceType.BRICK) - 1);
            currentPlayer.resources.put(ResourceType.LUMBER, currentPlayer.resources.get(ResourceType.LUMBER) - 1);
            e.setOccupied(true);
            e.setOwner(currentPlayer);
            currentPlayer.roads.add(new Road(currentPlayer, e));
            return "Road built successfully";

        }
    }

    public boolean isEdgeAvailableToBuild(Edge e){
        // check whether edge is adjacent to another edge or corner building owned by player
        // first look at adjacent corners. whether they are occupied by opponent player we can not build.
        // if not occupied by opponent player then we can build road
        // second look at edges adjacent to the corners. If they are occupied by our player then we can build road
        return false;
    }

    public String handleBuildSettlement(Corner c) {
        // TODO: after building settlement, check if player has longest road. Rerun CalculateLongestRoad() function
        if (currentPlayer.resources.get(ResourceType.BRICK) < 1 || currentPlayer.resources.get(ResourceType.LUMBER) < 1
                || currentPlayer.resources.get(ResourceType.WOOL) < 1 || currentPlayer.resources.get(ResourceType.GRAIN) < 1) {
            return "Not enough resources to build settlement";
        } else if (c.getIsOccupied()) {
            return "Corner is already occupied";
        }
        else if (!isCornerAvailableToBuild(c)) {
            return "Corner is not adjacent to any of your buildings";
        }
        else {
            currentPlayer.resources.put(ResourceType.BRICK, currentPlayer.resources.get(ResourceType.BRICK) - 1);
            currentPlayer.resources.put(ResourceType.LUMBER, currentPlayer.resources.get(ResourceType.LUMBER) - 1);
            currentPlayer.resources.put(ResourceType.WOOL, currentPlayer.resources.get(ResourceType.WOOL) - 1);
            currentPlayer.resources.put(ResourceType.GRAIN, currentPlayer.resources.get(ResourceType.GRAIN) - 1);
            c.setIsOccupied(true);
            c.setOwner(currentPlayer);
            currentPlayer.buildings.add(new Building(BuildingType.SETTLEMENT, currentPlayer, board.getAdjacentTilesOfCorner(c), c));
            return "Settlement built successfully";

        }
    }

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

    public String handleBuildCity(Corner c) {
        if (currentPlayer.resources.get(ResourceType.ORE) < 3 || currentPlayer.resources.get(ResourceType.GRAIN) < 2) {
            return "Not enough resources to build city";
        } else if (c.getBuildingType() != BuildingType.SETTLEMENT || c.getOwner() != currentPlayer) {
            return "Corner is not a settlement owned by current player";
        }
        else {
            currentPlayer.resources.put(ResourceType.ORE, currentPlayer.resources.get(ResourceType.ORE) - 3);
            currentPlayer.resources.put(ResourceType.GRAIN, currentPlayer.resources.get(ResourceType.GRAIN) - 2);
            c.setBuildingType(BuildingType.CITY);
            // TODO: update the buildingType of the building in currentPlayer.buildings
            return "City built successfully";

        }
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

}
