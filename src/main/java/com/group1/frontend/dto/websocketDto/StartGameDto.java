package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("START_GAME")
public class StartGameDto implements WebSocketDto{
    private String roomCode;
}
