package com.group1.frontend.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.group1.frontend.components.Board;
import com.group1.frontend.components.Game;

import java.io.IOException;

public class JsonSerializers {
    public static class BoardSerializer extends StdSerializer<Board>{
        public BoardSerializer() {
            this(null);
        }

        public BoardSerializer(Class<Board> t) {
            super(t);
        }

        @Override
        public void serialize(Board board, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("tiles", board.getTiles());
            jsonGenerator.writeObjectField("corners", board.getCorners());
            jsonGenerator.writeObjectField("edges", board.getEdges());
            jsonGenerator.writeEndObject();
        }
    }
    public static class GameSerializer extends StdSerializer<Game>{
        public GameSerializer() {
            this(null);
        }

        public GameSerializer(Class<Game> t) {
            super(t);
        }

        @Override
        public void serialize(Game game, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("players", game.getPlayers());
            jsonGenerator.writeObjectField("board", game.getBoard());
            jsonGenerator.writeObjectField("occupiedBuildings", game.getOccupiedBuildings());
            jsonGenerator.writeObjectField("currentPlayer", game.getCurrentPlayer());
            jsonGenerator.writeObjectField("turnNumber", game.getTurnNumber());
            jsonGenerator.writeObjectField("currentDiceRoll", game.getCurrentDiceRoll());
            jsonGenerator.writeEndObject();
        }
    }
    public static class PlayerSerializer extends StdSerializer<com.group1.frontend.components.Player>{
        public PlayerSerializer() {
            this(null);
        }

        public PlayerSerializer(Class<com.group1.frontend.components.Player> t) {
            super(t);
        }

        @Override
        public void serialize(com.group1.frontend.components.Player player, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("resources", player.getResources());
            jsonGenerator.writeObjectField("buildings", player.getBuildings());
            jsonGenerator.writeObjectField("roads", player.getRoads());
            jsonGenerator.writeObjectField("color", player.getColor());
            jsonGenerator.writeObjectField("name", player.getName());
            jsonGenerator.writeObjectField("cpu", player.getCpu());
            jsonGenerator.writeObjectField("victoryPoint", player.getVictoryPoint());
            jsonGenerator.writeObjectField("longestRoad", player.getLongestRoad());
            jsonGenerator.writeEndObject();
        }
    }

}
