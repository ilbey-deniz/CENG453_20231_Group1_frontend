package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("TEXT_MESSAGE")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class TextMessageDto implements WebSocketDto {
    private String sender;
    private String message;
}
