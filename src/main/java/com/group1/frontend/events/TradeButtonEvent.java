package com.group1.frontend.events;

import com.group1.frontend.enums.ResourceType;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class TradeButtonEvent extends Event{
    public static final EventType<TradeButtonEvent> TRADE_INIT_ACCEPT = new EventType<>(Event.ANY, "TRADE_INIT_ACCEPT");
    public static final EventType<TradeButtonEvent> TRADE_INIT_CANCEL = new EventType<>(Event.ANY, "TRADE_INIT_CANCEL");
    public static final EventType<TradeButtonEvent> TRADE_OFFER_ACCEPT = new EventType<>(Event.ANY, "TRADE_OFFER_ACCEPT");
    public static final EventType<TradeButtonEvent> TRADE_OFFER_CANCEL = new EventType<>(Event.ANY, "TRADE_OFFER_CANCEL");

    private HashMap<ResourceType, Integer> inResources;
    private HashMap<ResourceType, Integer> outResources;
    public TradeButtonEvent(EventType<? extends Event> eventType,
                            HashMap<ResourceType, Integer> inResources,
                            HashMap<ResourceType, Integer> outResources) {
        super(eventType);
        this.inResources = inResources;
        this.outResources = outResources;
    }
    public TradeButtonEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }


}
