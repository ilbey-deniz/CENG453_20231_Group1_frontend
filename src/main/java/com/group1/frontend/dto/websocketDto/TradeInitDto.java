package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.utils.JsonDeserializers;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonTypeName("TRADE_INIT")
//DON'T EVER NEVER EVER add @AllArgsConstructor
@JsonDeserialize(using = JsonDeserializers.TradeInitDtoDeserializer.class)
public class TradeInitDto implements WebSocketDto{
    private String offererName;
    private HashMap<ResourceType, Integer> offeredResources;
    private HashMap<ResourceType, Integer> requestedResources;
}
