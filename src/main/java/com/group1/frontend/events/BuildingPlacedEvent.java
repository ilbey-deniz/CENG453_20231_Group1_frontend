package com.group1.frontend.events;
import com.group1.frontend.components.Corner;
import com.group1.frontend.components.Edge;
import com.group1.frontend.components.Player;
import com.group1.frontend.enums.BuildingType;
import javafx.event.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingPlacedEvent extends Event {
    public static final EventType<BuildingPlacedEvent> ROAD_PLACED = new EventType<>(Event.ANY, "ROAD_PLACED");
    public static final EventType<BuildingPlacedEvent> CITY_PLACED = new EventType<>(Event.ANY, "CITY_PLACED");
    public static final EventType<BuildingPlacedEvent> SETTLEMENT_PLACED = new EventType<>(Event.ANY, "SETTLEMENT_PLACED");
    public static final EventType<BuildingPlacedEvent> CPU_ROAD_PLACED = new EventType<>(Event.ANY, "CPU_ROAD_PLACED");
    public static final EventType<BuildingPlacedEvent> CPU_CITY_PLACED = new EventType<>(Event.ANY, "CPU_CITY_PLACED");
    public static final EventType<BuildingPlacedEvent> CPU_SETTLEMENT_PLACED = new EventType<>(Event.ANY, "CPU_SETTLEMENT_PLACED");

    private final Edge edge;
    private final Corner corner;

    private final Player player;
    public BuildingPlacedEvent(EventType<? extends Event> eventType ,Object placement, Player player) {
        super(eventType);
        if (eventType == ROAD_PLACED || eventType == CPU_ROAD_PLACED) {
            this.edge = (Edge) placement;
            this.corner = null;
        } else {
            this.corner = (Corner) placement;
            this.edge = null;
        }
        this.player = player;

    }

}
