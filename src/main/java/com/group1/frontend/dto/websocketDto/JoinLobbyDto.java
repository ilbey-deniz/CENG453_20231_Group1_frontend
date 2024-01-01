package com.group1.frontend.dto.websocketDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.utils.LobbyPlayer;
import lombok.*;

@Data
@JsonTypeName("JOIN_LOBBY")
public class JoinLobbyDto implements WebSocketDto{
//    private String roomCode;
    private LobbyPlayer player;
}
