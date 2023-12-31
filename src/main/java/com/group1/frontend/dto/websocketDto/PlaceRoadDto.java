package com.group1.frontend.dto.websocketDto;

import com.group1.frontend.components.Edge;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceRoadDto implements MessageContent{
    private Edge edge;
}
