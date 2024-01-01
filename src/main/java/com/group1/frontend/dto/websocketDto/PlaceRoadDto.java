package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.group1.frontend.components.Edge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonTypeName("PLACE_ROAD")
public class PlaceRoadDto implements WebSocketDto{
    private Edge edge;
}
