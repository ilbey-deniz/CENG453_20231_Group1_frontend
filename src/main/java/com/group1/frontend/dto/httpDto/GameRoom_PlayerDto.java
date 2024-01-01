package com.group1.frontend.dto.httpDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameRoom_PlayerDto implements HttpRequestDto {
    String roomCode;
    PlayerDto player;
}
