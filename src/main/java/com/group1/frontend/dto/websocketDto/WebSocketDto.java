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
})
//DON'T EVER NEVER EVER add @AllArgsConstructor to the classes that implement this class
public interface WebSocketDto {

}
