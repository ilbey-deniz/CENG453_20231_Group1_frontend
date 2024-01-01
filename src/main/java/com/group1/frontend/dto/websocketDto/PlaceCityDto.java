package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.components.Corner;
import lombok.Data;

@Data
@JsonTypeName("PLACE_CITY")
public class PlaceCityDto implements WebSocketDto {
    // it might be better to use coordinate values instead of Corner
    private Corner corner;
}
