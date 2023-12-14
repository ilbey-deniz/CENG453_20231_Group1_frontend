package com.group1.frontend.components;

import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.events.*;
import com.group1.frontend.exceptions.DiceAlreadyRolledException;
import com.group1.frontend.utils.BoardUtilityFunctions;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.group1.frontend.constants.BoardConstants.REQUIRED_RESOURCES;

public class Game extends AnchorPane {
    private List<Player> players;
    private Board board;

    private final HashSet<Building> occupiedBuildings;
    private Player currentPlayer;
    private int turnNumber;

    private Pair<Integer, Integer> currentDiceRoll;

    private final HashMap<Player, Integer> playersWithLongestRoad;


    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayer = players.get(0);
        this.turnNumber = 0;
        this.occupiedBuildings = new HashSet<>();
        this.playersWithLongestRoad = new HashMap<>();
    }
    public Game() {
        this.players = new ArrayList<>();
        this.board = new Board();
        this.currentPlayer = null;
        this.turnNumber = 0;
        this.occupiedBuildings = new HashSet<>();
        this.playersWithLongestRoad = new HashMap<>();
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

    public void createInitialBuildings() {
        for(Player player : players) {
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

    public Pair<Integer, Integer> rollDice(){
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        currentDiceRoll = new Pair<>(dice1, dice2);
        return new Pair<>(dice1, dice2);
    }

    public void endTurn() {
        currentDiceRoll = null;
        //currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
        turnNumber++;
        currentPlayer = players.get(turnNumber % players.size());
    }

    public void placeSettlement(Corner corner) {
        corner.setIsOccupied(true);
        corner.setOwner(currentPlayer);
        REQUIRED_RESOURCES.get(BuildingType.SETTLEMENT).forEach(currentPlayer::removeResource);
        Building building = new Building(BuildingType.SETTLEMENT, currentPlayer, board.getAdjacentTilesOfCorner(corner), corner);
        currentPlayer.buildings.add(building);
        occupiedBuildings.add(building);
        currentPlayer.addVictoryPoint(1);
    }

    public void placeSettlementForFree(Corner corner, Player player) {
        corner.setIsOccupied(true);
        corner.setOwner(player);
        List<Tile> adjacentTiles = board.getAdjacentTilesOfCorner(corner);
        Building building = new Building(BuildingType.SETTLEMENT, player, adjacentTiles, corner);
        player.getBuildings().add(building);
        occupiedBuildings.add(building);
        for(Tile tile : adjacentTiles) {
            if (tile.getDiceNumber() != 7){
                player.addResource(tile.getTileType().getResourceType(), 1);
            }
        }
    }

    public void placeCity(Corner corner) {

        getBuildingFromCorner(corner).setBuildingType(BuildingType.CITY);
        REQUIRED_RESOURCES.get(BuildingType.CITY).forEach(currentPlayer::removeResource);
        currentPlayer.addVictoryPoint(1);
    }

    public Building getBuildingFromCorner(Corner corner) {
        for(Building building : occupiedBuildings) {
            if (building.getCorner().equals(corner)) {
                return building;
            }
        }
        return null;
    }

    public void placeRoad(Edge edge) {
        edge.setOccupied(true);
        edge.setOwner(currentPlayer);
        REQUIRED_RESOURCES.get(BuildingType.ROAD).forEach(currentPlayer::removeResource);
        currentPlayer.roads.add(new Road(currentPlayer, edge));
        System.out.println("------------------");
        System.out.println("player road count is " + currentPlayer.roads.size());
        System.out.println("longest road is: " + calculateLongestRoad(currentPlayer));
    }

    public void placeRoadForFree(Edge randomEdge, Player player) {
        randomEdge.setOccupied(true);
        randomEdge.setOwner(player);
        player.roads.add(new Road(player, randomEdge));
        System.out.println("longest road is: " + calculateLongestRoad(player));
    }

    public void distributeResources(int diceRoll) {
        if (diceRoll == 7) {
            return;
        }
        for(Building building : occupiedBuildings) {
            if (building.getCorner().getIsOccupied()) {
                for(Tile tile : board.getAdjacentTilesOfCorner(building.getCorner())) {
                    if (tile.getDiceNumber() == diceRoll) {
                        if (building.getBuildingType() == BuildingType.CITY) {
                            building.getOwner().addResource(tile.getTileType().getResourceType(), 2);
                        }
                        else {
                            building.getOwner().addResource(tile.getTileType().getResourceType(), 1);
                        }
                    }
                }
                // TODO: fire obtainedResource event to parent
            }
        }
    }

    public int calculateLongestRoad(Player player) {
        int maxRoadLength = 0;
        List<Corner> getEndCorners = getEndCorners(player);
        HashSet<Corner> allRoadCorners = getAllCornerOfRoads(player);
        for(Corner corner : getEndCorners){
            HashMap<Corner, Boolean> visited = new HashMap<>();
            for(Corner r : allRoadCorners) {
                visited.put(r, false);
            }
            System.out.println("Calculate based on endEdge -> " + corner.getXCoordinate() + " " + corner.getYCoordinate());
            int length = calculateTotalRoadLength(corner, visited, allRoadCorners);
            System.out.println("length: " + length);
            maxRoadLength = Math.max(maxRoadLength, length);

        }
        return maxRoadLength;
    }

    // take the starting point as parameter
    public int calculateTotalRoadLength(Corner corner, HashMap<Corner, Boolean> visited, HashSet<Corner> allRoadCorners) {
        visited.put(corner, true);
        System.out.println("corner: " + corner.getXCoordinate() + " " + corner.getYCoordinate() + " visited");
        List<Corner> adjacentCorners = board.getAdjacentCornersOfCorner(corner);
        int maxRoadLength = 0;
        for(Corner c : allRoadCorners) {
            if (!visited.get(c)){
                // if the road is next to the starting road
//                if(((road.edge.getFirstXCoordinate() == r.edge.getFirstXCoordinate()) && (road.edge.getFirstYCoordinate() == r.edge.getFirstYCoordinate()))
//                        || (road.edge.getSecondXCoordinate() == r.edge.getSecondXCoordinate() && road.edge.getSecondYCoordinate() == r.edge.getSecondYCoordinate())
//                        || (road.edge.getFirstXCoordinate() == r.edge.getSecondXCoordinate() && road.edge.getFirstYCoordinate() == r.edge.getSecondYCoordinate())
//                        || (road.edge.getSecondXCoordinate() == r.edge.getFirstXCoordinate() && road.edge.getSecondYCoordinate() == r.edge.getFirstYCoordinate()))
                for(Corner adjCorner : adjacentCorners){
                    if(((c.getXCoordinate() == adjCorner.getXCoordinate()) && (c.getYCoordinate() == adjCorner.getYCoordinate()))){
                        maxRoadLength = Math.max(maxRoadLength, 1 + calculateTotalRoadLength(adjCorner, visited, allRoadCorners));
                    }
                }
            }
        }
        return maxRoadLength;
    }

    public List<Corner> getEndCorners(Player player) {
        List<Corner> endCorners = new ArrayList<>();
        for (Road road : player.roads) {
            int first_counter = 0;
            int second_counter = 0;
            for (Road otherRoad : player.roads) {
                if (road.edge.getFirstXCoordinate() == otherRoad.edge.getSecondXCoordinate()
                        && road.edge.getFirstYCoordinate() == otherRoad.edge.getSecondYCoordinate()) {
                    first_counter++;
                }
                if (road.edge.getFirstXCoordinate() == otherRoad.edge.getFirstXCoordinate()
                        && road.edge.getFirstYCoordinate() == otherRoad.edge.getFirstYCoordinate()) {
                    first_counter++;
                }
                if (road.edge.getSecondXCoordinate() == otherRoad.edge.getSecondXCoordinate()
                        && road.edge.getSecondYCoordinate() == otherRoad.edge.getSecondYCoordinate()) {
                    second_counter++;
                }
                if (road.edge.getSecondYCoordinate() == otherRoad.edge.getFirstYCoordinate()
                        && road.edge.getSecondXCoordinate() == otherRoad.edge.getFirstXCoordinate()) {
                    second_counter++;
                }
            }

            if(first_counter == 1){
                Corner corner = board.getCornersAsMap().get(
                        Arrays.asList(road.edge.getFirstXCoordinate(), road.edge.getFirstYCoordinate()));
                endCorners.add(corner);
            }
            if(second_counter == 1){
                Corner corner = board.getCornersAsMap().get(
                        Arrays.asList(road.edge.getSecondXCoordinate(), road.edge.getSecondYCoordinate()));
                endCorners.add(corner);
            }
        }
//        for (Road e : endRoads) {
//            board.getAdjacentTilesOfCorner(new Corner(e.edge.getFirstXCoordinate(), e.edge.getFirstYCoordinate())).
//                    forEach(tile -> System.out.print(tile.getTileType() + " "));
//            System.out.println();
//        }
        System.out.println("end corner size: " + endCorners.size());
        return endCorners;
    }

    public HashSet<Corner> getAllCornerOfRoads(Player player) {
        HashSet<Corner> corners = new HashSet<>();
        for (Road road : player.roads) {
            corners.add(board.getCornersAsMap().get(
                    Arrays.asList(road.edge.getFirstXCoordinate(), road.edge.getFirstYCoordinate())));
            corners.add(board.getCornersAsMap().get(
                    Arrays.asList(road.edge.getSecondXCoordinate(), road.edge.getSecondYCoordinate())));
        }
        return corners;
    }

    public void updateAllVictoryPoints() {
        for(Player player : players) {
            int victoryPoint = 0;
            for(Building building : player.buildings) {
                if (building.getBuildingType() == BuildingType.SETTLEMENT) {
                    victoryPoint++;
                }
                else if (building.getBuildingType() == BuildingType.CITY) {
                    victoryPoint += 2;
                }
            }
            if(playersWithLongestRoad.containsKey(player)){
                victoryPoint += 2;
            }
            player.setVictoryPoint(victoryPoint);
        }
    }
    public void autoPlayCpuPlayer() {
        if(currentPlayer.isCpu()){
            DiceRolledEvent diceRolledEvent = new DiceRolledEvent(DiceRolledEvent.DICE_ROLLED);
            getParent().fireEvent(diceRolledEvent);

            if(currentPlayer.hasEnoughResources(BuildingType.CITY)){
                List <Building> availableBuildings = new ArrayList<>();
                for(Building building : currentPlayer.buildings){
                    if(building.getBuildingType() == BuildingType.SETTLEMENT){
                        availableBuildings.add(building);
                    }
                }
                if(!availableBuildings.isEmpty()){
                    Building randomBuilding = availableBuildings.get((int) (Math.random() * availableBuildings.size()));
                    getParent().fireEvent(new BuildingPlacedEvent(randomBuilding.getCorner(), BuildingType.CITY, currentPlayer));
                }

            }
            if(currentPlayer.hasEnoughResources(BuildingType.SETTLEMENT)){
                List<Corner> availableCorners = getAvailableCorners();
                if(!availableCorners.isEmpty()){
                    Corner randomCorner = availableCorners.get((int) (Math.random() * availableCorners.size()));
                    getParent().fireEvent(new BuildingPlacedEvent(randomCorner, BuildingType.SETTLEMENT, currentPlayer));
                }
            }
            Random random = new Random();
            if(random.nextGaussian() > 0.5){
                if(currentPlayer.hasEnoughResources(BuildingType.ROAD)){
                    List<Edge> availableEdges = getAvailableEdges();
                    if(!availableEdges.isEmpty()){
                        Edge randomEdge = availableEdges.get((int) (Math.random() * availableEdges.size()));
                        getParent().fireEvent(new BuildingPlacedEvent(randomEdge, BuildingType.ROAD, currentPlayer));
                        fireLongestRoadEventInNeed(currentPlayer.getLongestRoad());
                    }
                }
            }
            getParent().fireEvent(new TurnEndedEvent(TurnEndedEvent.TURN_ENDED));
        }
    }

    public void fireLongestRoadEventInNeed(Integer longestRoad){
        //fires LongestRoadEvent if the longest road is changed
        if(longestRoad>=5){
            if (playersWithLongestRoad.containsKey(currentPlayer) &&
                    playersWithLongestRoad.get(currentPlayer) < longestRoad ||
                    !playersWithLongestRoad.containsKey(currentPlayer) &&
                            !playersWithLongestRoad.isEmpty() && playersWithLongestRoad.values().stream().findFirst().get() < longestRoad){

                playersWithLongestRoad.clear();
                playersWithLongestRoad.put(currentPlayer, longestRoad);
                getParent().fireEvent(new LongestRoadEvent(new ArrayList<>(playersWithLongestRoad.keySet())));
            } else if (!playersWithLongestRoad.containsKey(currentPlayer) &&
                    playersWithLongestRoad.isEmpty() ||
                    !playersWithLongestRoad.containsKey(currentPlayer) &&
                            playersWithLongestRoad.values().stream().findFirst().get().equals(longestRoad)) {

                playersWithLongestRoad.put(currentPlayer, longestRoad);
                getParent().fireEvent(new LongestRoadEvent(new ArrayList<>(playersWithLongestRoad.keySet())));
            }

        }
    }
    public Pair<Integer, Integer> getCurrentDiceRoll() {
        return currentDiceRoll;
    }
    public Player checkWinner() {
        for(Player player : players) {
            if (player.getVictoryPoint() >= 8) {
                return player;
            }
        }
        return null;
    }
}
