package com.group1.frontend.dto.websocketDto;

import com.group1.frontend.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketDto {
    MessageType type;
    MessageContent content;
}
