package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.group1.frontend.enums.MessageType;
import lombok.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = KickPlayerDto.class),
        @JsonSubTypes.Type(value = JoinLobbyDto.class)
})
public interface WebSocketDto {

}
