package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = KickPlayerDto.class),
        @JsonSubTypes.Type(value = JoinLobbyDto.class),
        @JsonSubTypes.Type(value = LeaveGameDto.class),
        @JsonSubTypes.Type(value = PlayerReadyDto.class),
        @JsonSubTypes.Type(value = StartGameDto.class),
        @JsonSubTypes.Type(value = GameDto.class),
        @JsonSubTypes.Type(value = DiceRollDto.class),
        @JsonSubTypes.Type(value = PlaceBuildingDto.class),
        @JsonSubTypes.Type(value = TradeInitDto.class),
        @JsonSubTypes.Type(value = TradeAcceptDto.class),
        @JsonSubTypes.Type(value = EndTurnDto.class),
})
//DON'T EVER NEVER EVER add @AllArgsConstructor to the classes that implement this class
public interface WebSocketDto {

}
