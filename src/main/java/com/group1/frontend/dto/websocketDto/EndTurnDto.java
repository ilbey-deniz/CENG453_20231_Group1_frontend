package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("END_TURN")
public class EndTurnDto implements WebSocketDto{
//    private String playerName;
}
