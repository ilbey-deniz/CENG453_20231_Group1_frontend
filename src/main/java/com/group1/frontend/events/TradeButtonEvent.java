package com.group1.frontend.events;

import com.group1.frontend.enums.ResourceType;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class TradeButtonEvent extends Event{
    public static final EventType<TradeButtonEvent> TRADE_INIT_ACCEPT = new EventType<>(Event.ANY, "TRADE_INIT_ACCEPT");
    public static final EventType<TradeButtonEvent> TRADE_INIT_CANCEL = new EventType<>(Event.ANY, "TRADE_INIT_CANCEL");
    public static final EventType<TradeButtonEvent> TRADE_OFFER_ACCEPT = new EventType<>(Event.ANY, "TRADE_OFFER_ACCEPT");
    public static final EventType<TradeButtonEvent> TRADE_OFFER_CANCEL = new EventType<>(Event.ANY, "TRADE_OFFER_CANCEL");

    private String offererName;
    private String receiverName;
    private HashMap<ResourceType, Integer> requestedResources;
    private HashMap<ResourceType, Integer> offeredResources;
    public TradeButtonEvent(EventType<? extends Event> eventType,
                            HashMap<ResourceType, Integer> requestedResources,
                            HashMap<ResourceType, Integer> offeredResources) {
        super(eventType);
        this.requestedResources = requestedResources;
        this.offeredResources = offeredResources;
    }
    public TradeButtonEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }


}
