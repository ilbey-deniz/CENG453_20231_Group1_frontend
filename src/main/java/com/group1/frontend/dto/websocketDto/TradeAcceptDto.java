package com.group1.frontend.dto.websocketDto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.group1.frontend.enums.ResourceType;
import com.group1.frontend.utils.JsonDeserializers;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;

@Data
@JsonTypeName("TRADE_ACCEPT")
@JsonDeserialize(using = JsonDeserializers.TradeAcceptDtoDeserializer.class)
public class TradeAcceptDto implements WebSocketDto{
    private String traderName;
    private String tradeeName;
    private HashMap<ResourceType, Integer> offeredResources;
    private HashMap<ResourceType, Integer> requestedResources;
}
