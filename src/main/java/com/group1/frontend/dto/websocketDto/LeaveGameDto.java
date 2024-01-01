package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("LEAVE_GAME")
public class LeaveGameDto implements WebSocketDto{
//    private String playerName;
}
