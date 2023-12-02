package com.group1.frontend.view.elements;

import com.group1.frontend.components.Tile;
import com.group1.frontend.enums.ResourceType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.group1.frontend.constants.BoardConstants.TILE_HEIGHT;
import static com.group1.frontend.constants.BoardConstants.V;

public class TileView extends Polygon {
    Tile tile;


    public TileView(int diceNumber, ResourceType resourceType, double xCoordinate, double yCoordinate) throws FileNotFoundException {
        this.tile = new Tile(diceNumber, resourceType, xCoordinate, yCoordinate);
        this.tile.setXCoordinate( xCoordinate);
        this.tile.setYCoordinate( yCoordinate);
        getPoints().addAll(
                xCoordinate - (V * TILE_HEIGHT / 2), yCoordinate - (TILE_HEIGHT / 4),
                xCoordinate - (V * TILE_HEIGHT / 2), yCoordinate + (TILE_HEIGHT / 4),
                xCoordinate, yCoordinate + (TILE_HEIGHT / 2),
                xCoordinate + (V * TILE_HEIGHT / 2), yCoordinate + (TILE_HEIGHT / 4),
                xCoordinate + (V * TILE_HEIGHT / 2), yCoordinate - (TILE_HEIGHT / 4),
                xCoordinate, yCoordinate - (TILE_HEIGHT / 2)
        );
        // set up the visuals and a click listener for the tile
        if (tile.getResourceType() != null) {
            setFill(tile.getResourceType().getColor());
        }

        Image image = new Image(new FileInputStream(tile.getResourceType().getImagePath()));
        setFill(new ImagePattern(image));
        setStrokeWidth(2);
        setStroke(Color.BLACK);
        setOnMouseClicked(e -> System.out.println("Clicked: " + this));
        setEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, e -> setStroke(Color.RED));
        setEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, e -> setStroke(Color.BLACK));
    }



}
