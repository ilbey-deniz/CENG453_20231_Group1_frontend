package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.utils.LobbyPlayer;
import lombok.Data;


@Data
@JsonTypeName("KICK_PLAYER")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class KickPlayerDto implements WebSocketDto{
    private LobbyPlayer player;

}
