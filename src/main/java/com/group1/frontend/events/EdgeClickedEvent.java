package com.group1.frontend.events;
import com.group1.frontend.components.Edge;
import javafx.event.*;
import lombok.Getter;

@Getter
public class EdgeClickedEvent extends Event {
    public static final EventType<EdgeClickedEvent> EDGE_CLICKED = new EventType<>(Event.ANY, "EDGE_CLICKED");
    private Edge edge;
    public EdgeClickedEvent(Edge edge) {
        super(EDGE_CLICKED);
        this.edge = edge;
    }
    public EdgeClickedEvent() {
        super(EDGE_CLICKED);
    }
}
