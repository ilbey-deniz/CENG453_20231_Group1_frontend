package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("DICE_ROLL")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class DiceRollDto implements WebSocketDto{
    private int dice1;
    private int dice2;
}
