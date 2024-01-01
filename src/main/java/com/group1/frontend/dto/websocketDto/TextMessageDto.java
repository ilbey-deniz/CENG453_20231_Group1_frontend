package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("TEXT_MESSAGE")
public class TextMessageDto implements WebSocketDto {
    private String sender;
    private String message;
}
