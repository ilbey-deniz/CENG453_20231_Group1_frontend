package com.group1.frontend.dto.websocketDto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = KickPlayerDto.class, name = "KICK_PLAYER"),
//        @JsonSubTypes.Type(value = JoinLobbyDto.class, name = "JOIN_LOBBY")
//})

public interface MessageContent {

}
