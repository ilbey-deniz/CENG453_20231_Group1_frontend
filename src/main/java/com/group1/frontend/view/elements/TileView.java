package com.group1.frontend.view.elements;

import com.group1.frontend.components.Tile;
import com.group1.frontend.enums.ResourceType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static com.group1.frontend.constants.BoardConstants.TILE_HEIGHT;
import static com.group1.frontend.constants.BoardConstants.TILE_WIDTH;
import static com.group1.frontend.constants.BoardConstants.V;
import static com.group1.frontend.constants.BoardConstants.V2;

public class TileView extends Polygon {
    Tile tile;
    double xCoordinate;
    double yCoordinate;

    public TileView(int diceNumber, ResourceType resourceType, double xCoordinate, double yCoordinate) {
        this.tile = new Tile(diceNumber, resourceType);
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        getPoints().addAll(
                xCoordinate, yCoordinate,
                xCoordinate + TILE_WIDTH, yCoordinate,
                xCoordinate + TILE_WIDTH * 1.5, yCoordinate + TILE_HEIGHT * V,
                xCoordinate + TILE_WIDTH, yCoordinate + TILE_HEIGHT * V2,
                xCoordinate, yCoordinate + TILE_WIDTH * V2,
                xCoordinate - (TILE_WIDTH / 2.0), yCoordinate + TILE_HEIGHT * V
        );
        // set up the visuals and a click listener for the tile
        if (tile.getResourceType() != null) {
            setFill(tile.getResourceType().getColor());
        }
        setStrokeWidth(1);
        setStroke(Color.BLACK);
        setOnMouseClicked(e -> System.out.println("Clicked: " + this));
    }


}
