package com.group1.frontend.components;

import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.enums.TileType;
import com.group1.frontend.utils.IntegerPair;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;
    private List<Player> players;

    @Before
    public void setUp() throws Exception {
        this.game = new Game();
        // generate predefined players for testing the game
        Player player1 = new Player(PlayerColor.red, "player1", false);
        Player player2= new Player(PlayerColor.blue, "player2", false);
        Player player3 = new Player(PlayerColor.green, "player3", false);
        Player player4 = new Player(PlayerColor.yellow, "player4", false);
        players = List.of(player1, player2, player3, player4);
        game.setPlayers(players);
        game.setCurrentPlayer(player1);
        // generate initial board
        Board board = new Board();
        board.generateRandomBoard();
        game.setBoard(board);
        game.createInitialBuildings();
    }
    @Test
    public void testInitializeGame() {


        // set 1 building and road for each player

        // test if the game is initialized correctly
        assertEquals(game.getPlayers().size(), players.size());

        assertEquals(game.getBoard().getTiles().size(), 19);
        assertEquals(game.getBoard().getCorners().size(), 54);
        assertEquals(game.getBoard().getEdges().size(), 72);

        int occupiedBuildings = 0;
        for(Player player : game.getPlayers()) {
            occupiedBuildings += player.getBuildings().size();
        }
        assertEquals(game.getOccupiedBuildings().size(), occupiedBuildings);
    }

    @Test
    public void testPlayerResources(){

        // set 10 resources for each player
        for (Player player : game.getPlayers()) {
            for (ResourceType resourceType : ResourceType.values()) {
                player.addResource(resourceType, 10);
            }
        }

        // test initial resources
        for(Player player : game.getPlayers()){
            assertEquals(player.getResources().size(), 5);
            HashMap<ResourceType, Integer> totalResources = new HashMap<>();
            for(ResourceType resourceType: ResourceType.values()){
                totalResources.put(resourceType, 0);
            }
            for(Building building: player.getBuildings()){
                List<Tile> tiles = game.getBoard().getAdjacentTilesOfCorner(building.getCorner());
                for(Tile tile: tiles){
                    if(tile.getTileType() != TileType.DESERT){
                        ResourceType resourceType = tile.getTileType().getResourceType();
                        totalResources.put(resourceType, totalResources.get(resourceType) + 1);
                    }
                }
            }
            for(ResourceType resourceType: ResourceType.values()){
                Integer expected = totalResources.get(resourceType) + 10;
                Integer actual = player.getResources().get(resourceType);
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testDiceRoll() {
        // test if the dice roll is between 2 and 12
        for (int i = 0; i < 100; i++) {
            IntegerPair dicePair = game.rollDice();
            int totalRoll = dicePair.getFirst() + dicePair.getSecond();
            assertTrue(totalRoll >= 2 && totalRoll <= 12);
        }
    }

    @Test
    public void testDistributeResourcesWithDice() {
        int tryCount = 10;
        for (int i = 0; i < tryCount; i++) {
            // roll the dice
            IntegerPair dicePair = game.rollDice();
            int totalDiceNumber = dicePair.getFirst() + dicePair.getSecond();
            // distribute resources
            game.distributeResources(totalDiceNumber);
            // test if the resources are distributed correctly
            for (Player player : game.getPlayers()) {
                HashMap<ResourceType, Integer> initialResources = player.getResources();
                for (Building building : player.getBuildings()) {
                    List<Tile> tiles = game.getBoard().getAdjacentTilesOfCorner(building.getCorner());
                    for (Tile tile : tiles) {
                        if (tile.getTileType() != TileType.DESERT && tile.getDiceNumber() == totalDiceNumber) {
                            ResourceType resourceType = tile.getTileType().getResourceType();
                            Integer expected = initialResources.get(resourceType) + 1;
                            // update the initial resources
                            initialResources.put(resourceType, expected);
                            Integer actual = player.getResources().get(resourceType);
                            assertEquals(expected, actual);
                        }
                    }
                }
            }
        }
    }

}