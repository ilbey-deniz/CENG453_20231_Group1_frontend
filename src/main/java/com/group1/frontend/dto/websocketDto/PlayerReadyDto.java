package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.utils.LobbyPlayer;
import lombok.Data;

@Data
@JsonTypeName("PLAYER_READY")
public class PlayerReadyDto implements WebSocketDto{
    private LobbyPlayer player;
}
