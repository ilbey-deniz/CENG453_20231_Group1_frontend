package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.enums.BuildingType;
import lombok.Data;

import java.util.List;

@Data
@JsonTypeName("PLACE_BUILDING")
//DON'T EVER NEVER EVER add @AllArgsConstructor
public class PlaceBuildingDto implements WebSocketDto{
    // it might be better to use coordinate values instead of Corner
    private BuildingType buildingType;
    private String playerName;
    private List<Double> coordinates;
}
