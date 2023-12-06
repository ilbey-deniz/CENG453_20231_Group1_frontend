package com.group1.frontend.view.elements;

import com.group1.frontend.components.Corner;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import com.group1.frontend.events.CornerClickedEvent;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getRandomBuildingAsset;

public class CornerView extends Circle {
//    private double boardXCoordinate;
//    private double boardYCoordinate;
    Corner corner;

public CornerView(Corner corner) {
    super(corner.getXCoordinate() * TILE_HEIGHT*V2/4 * 1.10 + xStartOffset,
            corner.getYCoordinate() * TILE_HEIGHT*1/4 * 1.10 + yStartOffset, TILE_HEIGHT/8
    );
    setFill(Color.TRANSPARENT);
    this.corner = corner;


//    setFill(corner.getPlayer().getColor());
//    double xCoordinate = corner.getXCoordinate() * TILE_HEIGHT*V2/4 * 1.10 + xStartOffset;
//    double yCoordinate = corner.getYCoordinate() * TILE_HEIGHT*1/4 * 1.10 + yStartOffset;
//    this.boardXCoordinate = xCoordinate;
//    this.boardYCoordinate = yCoordinate;
    setOnMouseClicked(e -> {
        try {
            this.occupyCorner();
            getParent().fireEvent(new CornerClickedEvent(this.corner));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    });
}
    public void occupyCorner() throws FileNotFoundException {
        Image image = new Image(new FileInputStream(getRandomBuildingAsset()));
        // TODO: get the building asset according to player
        setFill(new ImagePattern(image));
        corner.setIsOccupied(true);
    }

    public Corner getCorner() {
        return corner;
    }
}


