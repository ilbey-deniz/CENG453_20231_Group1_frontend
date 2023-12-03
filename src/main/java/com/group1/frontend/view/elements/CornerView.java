package com.group1.frontend.view.elements;

import com.group1.frontend.components.Corner;
import javafx.scene.shape.Circle;

import static com.group1.frontend.constants.BoardConstants.*;

public class CornerView extends Circle {
    private double boardXCoordinate;
    private double boardYCoordinate;

public CornerView(Corner corner) {
    super(corner.getXCoordinate() * TILE_HEIGHT*V2/4 * 1.10 + xStartOffset, corner.getYCoordinate() * TILE_HEIGHT*1/4 * 1.10 + yStartOffset, 10);
    double xCoordinate = corner.getXCoordinate() * TILE_HEIGHT*V2/2 * 1.10 + xStartOffset;
    double yCoordinate = corner.getYCoordinate() * TILE_HEIGHT*3/4 * 1.10 + yStartOffset;
    this.boardXCoordinate = xCoordinate;
    this.boardYCoordinate = yCoordinate;
    setOnMouseClicked(e -> System.out.println("Clicked: " + this));
    }

}
