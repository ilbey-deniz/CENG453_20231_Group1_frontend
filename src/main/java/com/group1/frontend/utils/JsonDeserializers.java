package com.group1.frontend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.group1.frontend.components.*;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.enums.TileType;

import java.util.*;

public class JsonDeserializers {
    public static class PlayersHashMapDeserializer extends JsonDeserializer<HashMap<String, LobbyPlayer>> {
        @Override
        public HashMap<String, LobbyPlayer> deserialize(JsonParser jsonParser,
                                                        DeserializationContext deserializationContext) throws java.io.IOException{

            HashMap<String, LobbyPlayer> players = new HashMap<>();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            for (JsonNode playerNode : node) {
                String name = playerNode.get("name").asText();
                PlayerColor color = PlayerColor.valueOf(playerNode.get("color").asText());
                Boolean cpu = playerNode.get("cpu").asBoolean();
                Boolean ready = playerNode.get("ready").asBoolean();
                players.put(name, new LobbyPlayer(color, name, cpu, ready));
            }
            return players;
        }
    }
    public static class LobbyPlayerDeserializer extends JsonDeserializer<LobbyPlayer> {
        @Override
        public LobbyPlayer deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext) throws java.io.IOException{

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            String name = node.get("name").asText();
            PlayerColor color = PlayerColor.valueOf(node.get("color").asText());
            Boolean cpu = node.get("cpu").asBoolean();
            Boolean ready = node.get("ready").asBoolean();
            return new LobbyPlayer(color, name, cpu, ready);
        }
    }
    public static class PlayerDeserializer extends JsonDeserializer<Player> {
        @Override
        public Player deserialize(JsonParser jsonParser,
                                  DeserializationContext deserializationContext) throws java.io.IOException{

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            HashMap<String, Integer> resourcesAsString = node.get("resources").traverse(jsonParser.getCodec()).readValueAs(HashMap.class);
            HashMap<ResourceType, Integer> resources = new HashMap<>();
            for (String resourceAsString : resourcesAsString.keySet()) {
                resources.put(ResourceType.valueOf(resourceAsString), resourcesAsString.get(resourceAsString));
            }
            ArrayList<LinkedHashMap> buildingsLHM = node.get("buildings").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            List<Building> buildings = new ArrayList<>();
            for (LinkedHashMap buildingLHM : buildingsLHM) {
                BuildingType buildingType = BuildingType.valueOf((String) buildingLHM.get("buildingType"));
                ArrayList<LinkedHashMap> tileLHM = (ArrayList<LinkedHashMap>) buildingLHM.get("tiles");
                List<Tile> tiles = new ArrayList<>();
                for (LinkedHashMap tile : tileLHM) {
                    TileType tileType = TileType.valueOf((String) tile.get("tileType"));
                    Integer number = (Integer) tile.get("diceNumber");
                    Double xCoordinate = (Double) tile.get("xcoordinate");
                    Double yCoordinate = (Double) tile.get("ycoordinate");
                    tiles.add(new Tile(number, tileType, xCoordinate, yCoordinate));
                }
                LinkedHashMap cornerLHM = (LinkedHashMap) buildingLHM.get("corner");
                Corner corner = new Corner((Double) cornerLHM.get("xcoordinate"), (Double) cornerLHM.get("ycoordinate"));
//                Player owner = buildingLHM.get("owner") == null ? null : this.deserialize(jsonParser, deserializationContext);
                buildings.add(new Building(buildingType, null, tiles, corner));
            }
            ArrayList<LinkedHashMap> roadsLHM = node.get("roads").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashSet<Road> roads = new HashSet<>();
            for (LinkedHashMap roadLHM : roadsLHM) {
                LinkedHashMap edgeLHM = (LinkedHashMap) roadLHM.get("edge");
                Edge edge = new Edge((Double) edgeLHM.get("firstXCoordinate"), (Double) edgeLHM.get("firstYCoordinate"),
                        (Double) edgeLHM.get("secondXCoordinate"), (Double) edgeLHM.get("secondYCoordinate"));
                roads.add(new Road(null, edge));
            }
            PlayerColor color = PlayerColor.valueOf(node.get("color").asText());
            String name = node.get("name").asText();
            Boolean cpu = node.get("cpu").asBoolean();
            Integer victoryPoint = node.get("victoryPoint").asInt();
            Integer longestRoad = node.get("longestRoad").asInt();
            return new Player(resources, buildings, roads, color, name, cpu, victoryPoint, longestRoad);
        }
    }
    public static class BuildingDeserializer extends JsonDeserializer<Building> {
        @Override
        public Building deserialize(JsonParser jsonParser,
                                    DeserializationContext deserializationContext) throws java.io.IOException{

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            BuildingType buildingType = BuildingType.valueOf(node.get("buildingType").asText());
            List<Tile> tiles = node.get("tiles").traverse(jsonParser.getCodec()).readValueAs(List.class);
            LinkedHashMap cornerLHM = node.get("corner").traverse(jsonParser.getCodec()).readValueAs(LinkedHashMap.class);
            Corner corner = new Corner((Double) cornerLHM.get("xCoordinate"), (Double) cornerLHM.get("yCoordinate"));
            return new Building(buildingType, null, tiles, corner);
        }
    }
    public static class RoadDeserializer extends JsonDeserializer<Road> {
        @Override
        public Road deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws java.io.IOException{
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            LinkedHashMap edgeLHM = node.get("edge").traverse(jsonParser.getCodec()).readValueAs(LinkedHashMap.class);
            Edge edge = new Edge((Double) edgeLHM.get("firstXCoordinate"), (Double) edgeLHM.get("firstYCoordinate"),
                    (Double) edgeLHM.get("secondXCoordinate"), (Double) edgeLHM.get("secondYCoordinate"));
            return new Road(null, edge);
        }
    }
    public static class BoardDeserializer extends JsonDeserializer<Board> {
        @Override
        public Board deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws java.io.IOException{
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            ArrayList<LinkedHashMap> tilesAsList = node.get("tiles").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Tile> tiles = new HashMap<>();
            for (LinkedHashMap tileLHM : tilesAsList) {
                TileType tileType = TileType.valueOf((String) tileLHM.get("tileType"));
                Integer number = (Integer) tileLHM.get("diceNumber");
                Double xCoordinate = (Double) tileLHM.get("xcoordinate");
                Double yCoordinate = (Double) tileLHM.get("ycoordinate");
                tiles.put(List.of(xCoordinate, yCoordinate), new Tile(number, tileType, xCoordinate, yCoordinate));
            }

            ArrayList<LinkedHashMap> cornersAsList = node.get("corners").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Corner> corners = new HashMap<>();
            for (LinkedHashMap cornerLHM : cornersAsList) {
                Double xCoordinate = (Double) cornerLHM.get("xcoordinate");
                Double yCoordinate = (Double) cornerLHM.get("ycoordinate");
                corners.put(List.of(xCoordinate, yCoordinate), new Corner(xCoordinate, yCoordinate));
            }

            ArrayList<LinkedHashMap> edgesAsList = node.get("edges").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Edge> edges = new HashMap<>();
            for (LinkedHashMap edgeLHM : edgesAsList) {
                Double firstXCoordinate = (Double) edgeLHM.get("firstXCoordinate");
                Double firstYCoordinate = (Double) edgeLHM.get("firstYCoordinate");
                Double secondXCoordinate = (Double) edgeLHM.get("secondXCoordinate");
                Double secondYCoordinate = (Double) edgeLHM.get("secondYCoordinate");
                edges.put(List.of(firstXCoordinate, firstYCoordinate, secondXCoordinate, secondYCoordinate),
                        new Edge(firstXCoordinate, firstYCoordinate, secondXCoordinate, secondYCoordinate));

            }

            return new Board(tiles, corners, edges);
        }
    }
    public static class GameDeserializer extends JsonDeserializer<Game> {
        @Override
        public Game deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws java.io.IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Board board = node.get("board").traverse(jsonParser.getCodec()).readValueAs(Board.class);
            ArrayList<Player> players = new ArrayList<>();
            for (JsonNode playerNode : node.get("players")) {
                players.add(playerNode.traverse(jsonParser.getCodec()).readValueAs(Player.class));
            }
            //replace player's tiles and corners with the ones in the board
            for (Player player : players) {
                for (Building playerBuilding : player.getBuildings()) {
                    for (Tile playerTile : playerBuilding.getTiles()) {
                        //tile set
                        List<Double> coordinates = playerTile.getCoordinates();
                        board.getTilesAsMap().get(coordinates);
                        playerBuilding.getTiles().set(playerBuilding.getTiles().indexOf(playerTile), board.getTilesAsMap().get(coordinates));
                    }
                    //corner set
                    List<Double> coordinates = List.of(playerBuilding.getCorner().getXCoordinate(),
                            playerBuilding.getCorner().getYCoordinate());
                    playerBuilding.setCorner(board.getCornersAsMap().get(coordinates));
                }
                for (Road road : player.getRoads()) {
                    //edge set
                    List<Double> coordinates = List.of(road.getEdge().getFirstXCoordinate(), road.getEdge().getFirstYCoordinate(),
                            road.getEdge().getSecondXCoordinate(), road.getEdge().getSecondYCoordinate());
                    road.setEdge(board.getEdgesAsMap().get(coordinates));
                }
            }
            HashSet<Building> occupiedBuildings = new HashSet<>();
            for (Player player : players) {
                occupiedBuildings.addAll(player.getBuildings());
            }

            Player currentPlayerTemp = node.get("currentPlayer").traverse(jsonParser.getCodec()).readValueAs(Player.class);
            Player currentPlayer = null;
            for (Player player : players) {
                if (player.getName().equals(currentPlayerTemp.getName())) {
                    currentPlayer = player;
                }
            }
            int turnNumber = node.get("turnNumber").asInt();
            IntegerPair currentDiceRoll = node.get("currentDiceRoll").traverse(jsonParser.getCodec()).readValueAs(IntegerPair.class);

            return new Game(players, board, occupiedBuildings, currentPlayer, turnNumber, currentDiceRoll);
        }
    }

}
