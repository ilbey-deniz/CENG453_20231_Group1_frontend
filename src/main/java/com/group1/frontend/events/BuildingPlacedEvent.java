package com.group1.frontend.events;
import com.group1.frontend.components.Player;
import com.group1.frontend.enums.BuildingType;
import javafx.event.*;

public class BuildingPlacedEvent extends Event {
    public static final EventType<BuildingPlacedEvent> ROAD_PLACED = new EventType<>(Event.ANY, "ROAD_PLACED");
    public static final EventType<BuildingPlacedEvent> CITY_PLACED = new EventType<>(Event.ANY, "CITY_PLACED");
    public static final EventType<BuildingPlacedEvent> SETTLEMENT_PLACED = new EventType<>(Event.ANY, "SETTLEMENT_PLACED");
    private Object placement;

    private Player player;
    public BuildingPlacedEvent(Object placement, BuildingType buildingType, Player player) {
        super(buildingType == BuildingType.CITY ? CITY_PLACED : (buildingType == BuildingType.ROAD ? ROAD_PLACED : SETTLEMENT_PLACED));
        this.placement = placement;
        this.player = player;

    }
    public Player getPlayer() {
        return player;
    }

    public Object getPlacement() {
        return placement;
    }

}
