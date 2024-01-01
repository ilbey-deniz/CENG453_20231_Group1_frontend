package com.group1.frontend.dto.websocketDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.utils.LobbyPlayer;
import lombok.*;

@Data
@JsonTypeName("JOIN_LOBBY")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class JoinLobbyDto implements WebSocketDto{
    private LobbyPlayer player;
}
