package com.group1.frontend.events;

import com.group1.frontend.components.Player;
import javafx.event.Event;
import javafx.event.EventType;

import java.util.List;

public class LongestRoadEvent extends Event {
    List<Player> playersWithLongestRoad;
    public static final EventType<LongestRoadEvent> LONGEST_ROAD = new EventType<>(Event.ANY, "LONGEST_ROAD");

    public LongestRoadEvent(List<Player> playersWithLongestRoad) {
        super(LONGEST_ROAD);
        this.playersWithLongestRoad = playersWithLongestRoad;
    }
    public List<Player> getPlayersWithLongestRoad() {
        return playersWithLongestRoad;
    }
}
