package com.group1.frontend.view.elements;

import com.group1.frontend.components.Corner;
import com.group1.frontend.enums.BuildingType;
import com.group1.frontend.enums.PlayerColor;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import com.group1.frontend.events.CornerClickedEvent;


import static com.group1.frontend.constants.BoardConstants.*;
import static com.group1.frontend.utils.BoardUtilityFunctions.getSettlementAsset;
import static com.group1.frontend.utils.BoardUtilityFunctions.getCityAsset;

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
        getParent().fireEvent(new CornerClickedEvent(this.corner));
    });
}
    public void occupyCorner(PlayerColor color, BuildingType buildingType){
        try{
            Image image = null;
            if(buildingType == BuildingType.SETTLEMENT){
                image = new Image(getSettlementAsset(color.toString()));
            }
            else if(buildingType == BuildingType.CITY){
                image = new Image(getCityAsset(color.toString()));
            }
            assert image != null;
            setFill(new ImagePattern(image));
            corner.setOccupied(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void highlightOccupied() {
        setStrokeWidth(5);
        setStroke(Color.valueOf(HIGHLIGHT_STROKE_COLOR));
    }

    public void unhighlightOccupied() {
        setStrokeWidth(0);
        setStroke(Color.TRANSPARENT);
    }


    public void highlight() {
        setStrokeWidth(5);
        setStroke(Color.valueOf(HIGHLIGHT_STROKE_COLOR));
        setFill(Color.valueOf(HIGHLIGHT_FILL_COLOR));
    }
    public void unhighlight() {
        setStrokeWidth(0);
        setStroke(Color.TRANSPARENT);
        setFill(Color.TRANSPARENT);
    }

    public Corner getCorner() {
        return corner;
    }
}


