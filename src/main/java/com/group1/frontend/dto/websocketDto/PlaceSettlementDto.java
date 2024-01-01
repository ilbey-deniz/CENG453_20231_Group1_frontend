package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.components.Corner;
import lombok.Data;

@Data
@JsonTypeName("PLACE_SETTLEMENT")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class PlaceSettlementDto implements WebSocketDto{
    // it might be better to use coordinate values instead of Corner

    private Corner corner;
}
