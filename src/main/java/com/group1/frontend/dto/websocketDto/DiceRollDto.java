package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("DICE_ROLL")
public class DiceRollDto implements WebSocketDto{
    private int dice1;
    private int dice2;
}
