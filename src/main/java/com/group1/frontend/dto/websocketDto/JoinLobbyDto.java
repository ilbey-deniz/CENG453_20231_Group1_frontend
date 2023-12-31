package com.group1.frontend.dto.websocketDto;

import com.group1.frontend.utils.LobbyPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinLobbyDto implements MessageContent {
//    private String roomCode;
    private LobbyPlayer player;
}
