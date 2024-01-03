package com.group1.frontend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.group1.frontend.components.*;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.PlayerColor;
import com.group1.frontend.enums.ResourceType;

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
            HashMap<ResourceType, Integer> resources = node.get("resources").traverse(jsonParser.getCodec()).readValueAs(HashMap.class);
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
            Corner corner = node.get("corner").traverse(jsonParser.getCodec()).readValueAs(Corner.class);
            return new Building(buildingType, null, tiles, corner);
        }
    }
    public static class RoadDeserializer extends JsonDeserializer<Road> {
        @Override
        public Road deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws java.io.IOException{
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Edge edge = node.get("edge").traverse(jsonParser.getCodec()).readValueAs(Edge.class);
            return new Road(null, edge);
        }
    }
    public static class BoardDeserializer extends JsonDeserializer<Board> {
        @Override
        public Board deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws java.io.IOException{
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            ArrayList<Tile> tilesAsList = node.get("tiles").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Tile> tiles = new HashMap<>();
            for (Tile tile : tilesAsList) {
                tiles.put(tile.getCoordinates(), tile);
            }

            ArrayList<Corner> cornersAsList = node.get("corners").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Corner> corners = new HashMap<>();
            for (Corner corner : cornersAsList) {
                corners.put(corner.getCoordinates(), corner);
            }

            ArrayList<Edge> edgesAsList = node.get("edges").traverse(jsonParser.getCodec()).readValueAs(ArrayList.class);
            HashMap<List<Double>, Edge> edges = new HashMap<>();
            for (Edge edge : edgesAsList) {
                edges.put(edge.getCoordinates(), edge);
            }

            return new Board(tiles, corners, edges);
        }
    }

}
