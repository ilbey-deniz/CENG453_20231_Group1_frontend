package com.group1.frontend.events;
import com.group1.frontend.components.Corner;
import javafx.event.*;
import lombok.Getter;

@Getter
public class CornerClickedEvent extends Event{
    public static final EventType<CornerClickedEvent> CORNER_CLICKED = new EventType<>(Event.ANY, "CORNER_CLICKED");
    private Corner corner;

    public CornerClickedEvent(Corner corner) {
        super(CORNER_CLICKED);
        this.corner = corner;
    }

    public CornerClickedEvent() {
        super(CORNER_CLICKED);
    }

}
