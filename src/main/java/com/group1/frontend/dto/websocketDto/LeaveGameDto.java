package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.utils.LobbyPlayer;
import lombok.Data;

@Data
@JsonTypeName("LEAVE_GAME")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class LeaveGameDto implements WebSocketDto{
//    private String playerName;
    private LobbyPlayer player;
}
