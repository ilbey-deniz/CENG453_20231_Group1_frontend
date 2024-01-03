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

}
