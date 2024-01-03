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
            List<Building> buildings = node.get("buildings").traverse(jsonParser.getCodec()).readValueAs(List.class);
            HashSet<Road> roads = node.get("roads").traverse(jsonParser.getCodec()).readValueAs(HashSet.class);
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

}
